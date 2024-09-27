package com.example.backend_notion_api.service;

import com.example.backend_notion_api.domain.entity.PageToPageDTO;
import com.example.backend_notion_api.domain.request.PageToPageUploadDTO;

import java.util.List;
import java.util.Map;

public interface PageToPageService {

    PageToPageDTO createPageToPage(String parentPagePath);

    PageToPageUploadDTO uploadPageToPage(PageToPageUploadDTO pageToPageUploadDTO);

    PageToPageDTO getPageToPage(String path);

    List<Map<String, Object>> getPageToPageList(String pageId);

    void deletePageToPage(String path);

    void deleteAllPageToPageByPageId(String pageId);
}

