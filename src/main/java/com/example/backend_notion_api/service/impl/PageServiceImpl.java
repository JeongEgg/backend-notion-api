package com.example.backend_notion_api.service.impl;

import com.example.backend_notion_api.domain.entity.PageDTO;
import com.example.backend_notion_api.domain.request.CompareUpdatedDateDTO;
import com.example.backend_notion_api.enums.IdType;
import com.example.backend_notion_api.exception.customexception.InvalidPageTypeException;
import com.example.backend_notion_api.exception.customexception.PageNotFoundException;
import com.example.backend_notion_api.mapper.PageMapper;
import com.example.backend_notion_api.s3.S3Service;
import com.example.backend_notion_api.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.backend_notion_api.pagecontent.PageContentProvider.*;

@Service
public class PageServiceImpl implements PageService {

    private static final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private S3Service s3Service;

    @Override
    public PageDTO createPage(String id, IdType idType, String pageType) {
        logger.info("사용자: {}, 페이지 타입: {}에 대한 페이지 생성 중", id, pageType);
        if (pageType.equals("default")) {
            PageDTO pageDTO = PageDTO.builder()
                    .userId(id)
                    .title(null)
                    .iconUrl(null)
                    .backgroundUrl(null)
                    .updatedDate(LocalDateTime.now())
                    .content(getDefaultContent())
                    .childrenPagePathList(null)
                    .build();
            pageMapper.insertPage(pageDTO);
            logger.info("페이지가 성공적으로 생성되었습니다: {}", pageDTO);
            return pageDTO;
        } else {
            logger.error("유효하지 않은 페이지 타입: {}", pageType);
            throw new InvalidPageTypeException(pageType);
        }
    }

    @Override
    public int countPageByIdAndUserId(String pageId, String userId) {
        logger.info("페이지 ID: {}, 사용자 ID: {}에 대한 페이지 수 세기", pageId, userId);
        Map<String, Object> params = new HashMap<>();
        params.put("pageId", pageId);
        params.put("userId", userId);
        if (pageMapper.countPageByIdAndUserId(params) == 0) {
            logger.warn("페이지를 찾을 수 없습니다: {}", pageId);
            throw new PageNotFoundException("페이지 ID : " + pageId);
        }
        return 0;
    }

    @Override
    public PageDTO uploadPage(PageDTO pageDTO) {
        logger.info("페이지 업로드 중: {}", pageDTO);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageId", pageDTO.getPageId());
        paramMap.put("userId", pageDTO.getUserId());

        int count = pageMapper.countPageByIdAndUserId(paramMap);
        pageDTO.setUpdatedDate(LocalDateTime.now());

        if (count > 0) {
            logger.info("기존 페이지 업데이트 중: {}", pageDTO);
            pageMapper.updatePage(pageDTO);
        } else {
            logger.info("새 페이지 삽입 중: {}", pageDTO);
            pageMapper.insertPage(pageDTO);
        }
        return pageDTO;
    }

    @Override
    public List<Map<String, Object>> getPageList(String userId) {
        logger.info("사용자 ID: {}에 대한 페이지 목록 가져오기", userId);
        return pageMapper.getPageListByUserId(userId);
    }

    @Override
    public void deletePage(String userId, String pageId) {
        logger.info("페이지 삭제 중, 페이지 ID: {}, 사용자 ID: {}", pageId, userId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageId", pageId);
        paramMap.put("userId", userId);

        int count = pageMapper.countPageByIdAndUserId(paramMap);
        if (count > 0) {
            pageMapper.deletePageByIdAndUserId(paramMap);
            s3Service.deleteObjectsWithPrefix(pageId + "/");
            logger.info("페이지 삭제 성공: {}", pageId);
        } else {
            logger.warn("삭제할 페이지를 찾을 수 없습니다: {}", pageId);
            throw new PageNotFoundException("해당 페이지를 찾을 수 없습니다.");
        }
    }

    @Override
    public String compareUpdatedDateByPageIdAndUserId(CompareUpdatedDateDTO compareUpdatedDateDTO) {
        logger.info("페이지 ID: {}, 사용자 ID: {}에 대한 업데이트 날짜 비교 중", compareUpdatedDateDTO.getPageId(), compareUpdatedDateDTO.getUserId());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", compareUpdatedDateDTO.getUserId());
        paramMap.put("pageId", compareUpdatedDateDTO.getPageId());
        paramMap.put("updatedDate", compareUpdatedDateDTO.getUpdatedDate());

        String comparisonResult = pageMapper.compareUpdatedDateByPageIdAndUserId(paramMap);
        logger.info("비교 결과: {}", comparisonResult);
        return comparisonResult;
    }

    @Override
    public PageDTO updatePage(PageDTO pageDTO) {
        logger.info("페이지 업데이트 중: {}", pageDTO);
        CompareUpdatedDateDTO compareUpdatedDateDTO = CompareUpdatedDateDTO.builder()
                .userId(pageDTO.getUserId())
                .pageId(pageDTO.getPageId())
                .updatedDate(pageDTO.getUpdatedDate())
                .build();

        if (compareUpdatedDateByPageIdAndUserId(compareUpdatedDateDTO).equals("client is later")) {
            pageMapper.updatePage(pageDTO);
            logger.info("페이지 업데이트 성공: {}", pageDTO);
            return pageDTO;
        } else {
            PageDTO pageDTOofDB = pageMapper.getPage(pageDTO.getUserId(), pageDTO.getPageId());
            logger.info("데이터베이스에서 페이지 반환: {}", pageDTOofDB);
            return pageDTOofDB;
        }
    }
}