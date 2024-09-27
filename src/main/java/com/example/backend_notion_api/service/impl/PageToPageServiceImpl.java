package com.example.backend_notion_api.service.impl;

import com.example.backend_notion_api.domain.entity.PageToPageDTO;
import com.example.backend_notion_api.domain.request.PageToPageUploadDTO;
import com.example.backend_notion_api.exception.customexception.PageToPageNotFoundException;
import com.example.backend_notion_api.mapper.PageToPageMapper;
import com.example.backend_notion_api.service.PageToPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageToPageServiceImpl implements PageToPageService {

    @Autowired
    private PageToPageMapper pageToPageMapper;

    @Override
    public PageToPageDTO createPageToPage(String parentPagePath) {
        int getCount = pageToPageMapper.getPageToPageCount(parentPagePath)+1;
        PageToPageDTO pageToPageDTO = PageToPageDTO.builder()
                .path(parentPagePath+"/"+getCount)
                .title(null)
                .content(null)
                .build();

        pageToPageMapper.insertPageToPage(pageToPageDTO);

        return pageToPageDTO;
    }

    @Override
    public PageToPageUploadDTO uploadPageToPage(PageToPageUploadDTO pageToPageUploadDTO) {

        pageToPageMapper.updatePageToPage(pageToPageUploadDTO);

        return PageToPageUploadDTO.builder()
                .path(pageToPageUploadDTO.getPath())
                .title(pageToPageUploadDTO.getTitle())
                .content(pageToPageUploadDTO.getContent())
                .build();
    }

    @Override
    public PageToPageDTO getPageToPage(String path) {
        PageToPageDTO pageToPageDTO = pageToPageMapper.getPageToPageByPath(path);

        if (pageToPageDTO == null) {
            throw new PageToPageNotFoundException(path);
        }

        return pageToPageDTO;
    }

    @Override
    public List<Map<String, Object>> getPageToPageList(String pageId) {
        List<PageToPageDTO> pageToPageList = pageToPageMapper.getAllPageToPageByPageId(pageId);

        return pageToPageList.stream()
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("pageToPageId", dto.getPageToPageId());
                    map.put("path", dto.getPath());
                    map.put("title", dto.getTitle());
                    map.put("content", dto.getContent());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deletePageToPage(String path) {
        pageToPageMapper.deletePageToPageByPath(path);
    }

    @Override
    public void deleteAllPageToPageByPageId(String pageId) {
        pageToPageMapper.deleteAllPageToPageByPageId(pageId);
    }
}

