package com.example.backend_notion_api.mapper;

import com.example.backend_notion_api.domain.entity.PageToPageDTO;
import com.example.backend_notion_api.domain.request.PageToPageUploadDTO;

import java.util.List;

public interface PageToPageMapper {

    void insertPageToPage(PageToPageDTO pageToPageDTO);

    int getPageToPageCount(String parentPageId);

    void updatePageToPage(PageToPageUploadDTO pageToPageUploadDTO);

    PageToPageDTO getPageToPageByPath(String path);

    List<PageToPageDTO> getAllPageToPageByPageId(String pageId);

    void deletePageToPageByPath(String path);

    void deleteAllPageToPageByPageId(String pageId);
}
