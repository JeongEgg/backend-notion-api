package com.example.backend_notion_api.service.impl;

import com.example.backend_notion_api.domain.entity.PageDTO;
import com.example.backend_notion_api.domain.request.CompareUpdatedDateDTO;
import com.example.backend_notion_api.enums.IdType;
import com.example.backend_notion_api.exception.customexception.InvalidPageTypeException;
import com.example.backend_notion_api.exception.customexception.PageNotFoundException;
import com.example.backend_notion_api.mapper.PageMapper;
import com.example.backend_notion_api.s3.S3Service;
import com.example.backend_notion_api.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.backend_notion_api.pagecontent.PageContentProvider.*;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private S3Service s3Service;

    @Override
    public PageDTO createPage(String id, IdType idType, String pageType) {
        if (pageType.equals("default")) {
            PageDTO pageDTO = PageDTO.builder()
                    // page_id는 자동 증가
                    .userId(id)
                    .title(null)
                    .iconUrl(null)
                    .backgroundUrl(null)
                    .updatedDate(LocalDateTime.now())
                    .content(getDefaultContent())
                    .childrenPagePathList(null)
                    .build();
            pageMapper.insertPage(pageDTO);
            return pageDTO;
        }else {
            throw new InvalidPageTypeException(pageType);
        }
    }

    @Override
    public int countPageByIdAndUserId(String pageId, String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageId", pageId);
        params.put("userId", userId);
        if (pageMapper.countPageByIdAndUserId(params) == 0) {
            throw new PageNotFoundException("Page Id : " + pageId);
        }
        return 0;
    }

    @Override
    public PageDTO uploadPage(PageDTO pageDTO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageId", pageDTO.getPageId());
        paramMap.put("userId", pageDTO.getUserId());

        int count = pageMapper.countPageByIdAndUserId(paramMap);

        pageDTO.setUpdatedDate(LocalDateTime.now());
        if (count > 0) {
            // 페이지 업데이트
            pageMapper.updatePage(pageDTO);
        } else {
            // 새로운 페이지 삽입
            /**
             * 새로운 페이지를 삽입하는 이유는 로컬의 페이지가 제대로
             * 업로드 되지 않은 경우가 발생했을 수 있기 때문이다.
             * 일반적으로는 서버에 페이지를 생성하도록 하는 것이 좋다.
             * */
            pageMapper.insertPage(pageDTO);
        }
        return pageDTO;
    }

    @Override
    public List<Map<String, Object>> getPageList(String userId) {
        return pageMapper.getPageListByUserId(userId);
    }

    @Override
    public void deletePage(String userId, String pageId) {
        // pageId와 userId로 삭제할 페이지가 있는지 확인
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageId", pageId);
        paramMap.put("userId", userId);

        // 페이지가 존재할 경우 삭제
        int count = pageMapper.countPageByIdAndUserId(paramMap);
        if (count > 0) {
            pageMapper.deletePageByIdAndUserId(paramMap);
            s3Service.deleteObjectsWithPrefix(pageId+"/");
        }else {
            throw new PageNotFoundException("해당 페이지를 찾을 수 없습니다.");
        }
    }

    @Override
    public String compareUpdatedDateByPageIdAndUserId(CompareUpdatedDateDTO compareUpdatedDateDTO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", compareUpdatedDateDTO.getUserId());
        paramMap.put("pageId", compareUpdatedDateDTO.getPageId());
        paramMap.put("updatedDate", compareUpdatedDateDTO.getUpdatedDate());

        String comparisonResult = pageMapper.compareUpdatedDateByPageIdAndUserId(paramMap);
        return comparisonResult;
    }

    @Override
    public PageDTO updatePage(PageDTO pageDTO) {

        CompareUpdatedDateDTO compareUpdatedDateDTO = CompareUpdatedDateDTO.builder()
                .userId(pageDTO.getUserId())
                .pageId(pageDTO.getPageId())
                .updatedDate(pageDTO.getUpdatedDate())
                .build();
        if (compareUpdatedDateByPageIdAndUserId(compareUpdatedDateDTO).equals("client is later")){
            pageMapper.updatePage(pageDTO);
            return pageDTO;
        }else {
            PageDTO pageDTOofDB = pageMapper.getPage(pageDTO.getUserId(), pageDTO.getPageId());
            return pageDTOofDB;
        }
    }
}
