package com.example.backend_notion_api.controller;

import com.example.backend_notion_api.domain.request.CompareUpdatedDateDTO;
import com.example.backend_notion_api.domain.request.PageDeleteDTO;
import com.example.backend_notion_api.domain.response.Api;
import com.example.backend_notion_api.domain.entity.PageDTO;
import com.example.backend_notion_api.domain.response.PageListDTO;
import com.example.backend_notion_api.enums.IdType;
import com.example.backend_notion_api.exception.customexception.UserInformationTamperedException;
import com.example.backend_notion_api.exception.customexception.UserNotFoundException;
import com.example.backend_notion_api.service.impl.PageServiceImpl;
import com.example.backend_notion_api.service.impl.PageToPageServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pages")
public class PageController {

    @Autowired
    private final PageServiceImpl pageService;

    @Autowired
    private final PageToPageServiceImpl pageToPageService;

    public PageController(PageServiceImpl pageService, PageToPageServiceImpl pageToPageService) {
        this.pageService = pageService;
        this.pageToPageService = pageToPageService;
    }


    @PostMapping("")
    public ResponseEntity<Api<PageDTO>> createPage(
            @RequestParam("page_type") String pageTYpe,
            Authentication authentication
    ){
        Api<PageDTO> response = null;
        String username = authentication.getName();

        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        PageDTO pageDTO = pageService.createPage(username, IdType.USER, pageTYpe);
        response = Api.<PageDTO>builder()
                .resultCode("200")
                .resultMessage("페이지가 생성되었습니다.")
                .data(pageDTO)
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<Api<PageDTO>> uploadPage(
            @Valid @RequestBody PageDTO pageDTO,
            Authentication authentication
    ){
        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        // authentication 객체에서 username 추출
        String authenticatedUsername = authentication.getName();

        // 인증된 사용자와 pageDTO의 userId가 일치하지 않는 경우 예외 발생
        if (!authenticatedUsername.equals(pageDTO.getUserId())) {
            throw new UserInformationTamperedException("인증된 사용자와 페이지의 사용자 ID가 일치하지 않습니다.");
        }

        PageDTO updatedPage = pageService.uploadPage(pageDTO);

        Api<PageDTO> response = Api.<PageDTO>builder()
                .resultCode("200")
                .resultMessage("페이지가 성공적으로 업로드되었습니다.")
                .data(updatedPage)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/page-list")
    public ResponseEntity<Api<List<PageListDTO>>> getPageList(
            @RequestParam("user_id") String userId,
            Authentication authentication
    ){
        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        // authentication 객체에서 username 추출
        String authenticatedUsername = authentication.getName();

        // 인증된 사용자와 pageDTO의 userId가 일치하지 않는 경우 예외 발생
        if (!authenticatedUsername.equals(userId)) {
            throw new UserInformationTamperedException("인증된 사용자와 페이지의 사용자 ID가 일치하지 않습니다.");
        }
        List<Map<String, Object>> pageList = pageService.getPageList(userId);

        // 가져온 pageList 데이터를 PageListResponseDTO로 변환
        List<PageListDTO> pages = pageList.stream()
                .map(page -> PageListDTO.builder()
                        .pageId((Long) page.get("page_id"))
                        .title((String) page.get("title"))
                        .build())
                .toList();

        // 성공 응답 생성
        Api<List<PageListDTO>> response = Api.<List<PageListDTO>>builder()
                .resultCode("200")
                .resultMessage("페이지 목록 조회 성공")
                .data(pages)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/compare-updated-date")
    public ResponseEntity<Api<String>> compareUpdatedDate(
            @RequestBody CompareUpdatedDateDTO compareUpdatedDateDTO,
            Authentication authentication
    ){
        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        // authentication 객체에서 username 추출
        String authenticatedUsername = authentication.getName();

        // 인증된 사용자와 pageDTO의 userId가 일치하지 않는 경우 예외 발생
        if (!authenticatedUsername.equals(compareUpdatedDateDTO.getUserId())) {
            throw new UserInformationTamperedException("인증된 사용자와 페이지의 사용자 ID가 일치하지 않습니다.");
        }
        String compare =  pageService.compareUpdatedDateByPageIdAndUserId(compareUpdatedDateDTO);
        Api<String> response = Api.<String>builder()
                .resultCode("200")
                .resultMessage("최종수정시간이 정상적으로 비교됨")
                .data(compare)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/latest-page")
    public ResponseEntity<Api<PageDTO>> compareAndGetLatestPage(
            @RequestBody PageDTO pageDTO,
            Authentication authentication
    ){
        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        // authentication 객체에서 username 추출
        String authenticatedUsername = authentication.getName();

        // 인증된 사용자와 pageDTO의 userId가 일치하지 않는 경우 예외 발생
        if (!authenticatedUsername.equals(pageDTO.getUserId())) {
            throw new UserInformationTamperedException("인증된 사용자와 페이지의 사용자 ID가 일치하지 않습니다.");
        }
        // 페이지 업데이트 및 비교
        PageDTO updatedPage = pageService.updatePage(pageDTO);

        // 성공 응답 생성
        Api<PageDTO> response = Api.<PageDTO>builder()
                .resultCode("200")
                .resultMessage("페이지 정보 조회 성공")
                .data(updatedPage)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/drop")
    public ResponseEntity<Void> deletePage(
            @RequestBody PageDeleteDTO pageDeleteDTO,
            Authentication authentication
    ){
        if (authentication == null || !authentication.isAuthenticated()){
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        // authentication 객체에서 username 추출
        String authenticatedUsername = authentication.getName();

        // 인증된 사용자와 pageDTO의 userId가 일치하지 않는 경우 예외 발생
        if (!authenticatedUsername.equals(pageDeleteDTO.getUserId())) {
            throw new UserInformationTamperedException("인증된 사용자와 페이지의 사용자 ID가 일치하지 않습니다.");
        }
        pageService.deletePage(pageDeleteDTO.getUserId(),
                                pageDeleteDTO.getPageId());
        // 페이지 하위의 페이지들도 함께 삭제함.
        pageToPageService.deleteAllPageToPageByPageId(pageDeleteDTO.getPageId());

        return ResponseEntity.noContent().build();
    }

}
