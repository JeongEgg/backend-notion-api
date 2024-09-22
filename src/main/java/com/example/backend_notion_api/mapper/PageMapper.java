package com.example.backend_notion_api.mapper;

import com.example.backend_notion_api.domain.entity.PageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PageMapper {

    // 페이지 정보를 DB에 삽입하는 메서드
    void insertPage(PageDTO pageDTO);

    // 페이지 업데이트
    void updatePage(PageDTO pageDTO);

    // page_id와 user_id로 페이지 카운트
    int countPageByIdAndUserId(Map<String, Object> map);

    // user_id로 페이지 리스트 가져오기
    List<Map<String, Object>> getPageListByUserId(String userId);

    // page_id와 user_id로 페이지 삭제
    void deletePageByIdAndUserId(Map<String, Object> map);

    // 업데이트 날짜 비교하는 메서드
    String compareUpdatedDateByPageIdAndUserId(Map<String, Object> map);

    // 페이지 정보를 조회하는 메서드
    PageDTO getPage(String userId, Long pageId);

}
