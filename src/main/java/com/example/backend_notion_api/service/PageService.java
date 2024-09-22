package com.example.backend_notion_api.service;

import com.example.backend_notion_api.domain.entity.PageDTO;
import com.example.backend_notion_api.domain.request.CompareUpdatedDateDTO;
import com.example.backend_notion_api.enums.IdType;

import java.util.List;
import java.util.Map;

public interface PageService {

    PageDTO createPage(String id, IdType idType, String pageType);

    int countPageByIdAndUserId(String pageId, String userId);

    PageDTO uploadPage(PageDTO pageDTO);

    List<Map<String, Object>> getPageList(String userId);

    void deletePage(String userId, String pageId);

    String compareUpdatedDateByPageIdAndUserId(CompareUpdatedDateDTO compareUpdatedDateDTO);

    PageDTO updatePage(PageDTO pageDTO);
}
