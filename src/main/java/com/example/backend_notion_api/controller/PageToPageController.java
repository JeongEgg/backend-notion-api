package com.example.backend_notion_api.controller;

import com.example.backend_notion_api.domain.entity.PageToPageDTO;
import com.example.backend_notion_api.domain.request.PageToPageUploadDTO;
import com.example.backend_notion_api.domain.response.Api;
import com.example.backend_notion_api.exception.customexception.UserNotFoundException;
import com.example.backend_notion_api.service.impl.PageToPageServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/page-to-page")
public class PageToPageController {
    private final PageToPageServiceImpl pageToPageService;

    public PageToPageController(PageToPageServiceImpl pageToPageService) {
        this.pageToPageService = pageToPageService;
    }

    @PostMapping("")
    public ResponseEntity<Api<PageToPageDTO>> createPageToPage(
            @RequestParam("parent_page_path") String parentPagePath,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        PageToPageDTO pageToPageDTO = pageToPageService.createPageToPage(parentPagePath);
        Api<PageToPageDTO> response = Api.<PageToPageDTO>builder()
                .resultCode("201")
                .resultMessage("페이지가 생성되었습니다.")
                .data(pageToPageDTO)
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<Api<PageToPageUploadDTO>> uploadPageToPage(
            @Valid @RequestBody PageToPageUploadDTO pageToPageUploadDTO,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        PageToPageUploadDTO updatedPageToPage = pageToPageService.uploadPageToPage(pageToPageUploadDTO);
        Api<PageToPageUploadDTO> response = Api.<PageToPageUploadDTO>builder()
                .resultCode("200")
                .resultMessage("페이지가 성공적으로 업로드되었습니다.")
                .data(updatedPageToPage)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-page")
    public ResponseEntity<Api<PageToPageDTO>> getPageToPage(
            @RequestParam String path,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        PageToPageDTO pageToPageDTO = pageToPageService.getPageToPage(path);
        Api<PageToPageDTO> response = Api.<PageToPageDTO>builder()
                .resultCode("200")
                .resultMessage("페이지 정보를 조회했습니다.")
                .data(pageToPageDTO)
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/list")
    public ResponseEntity<Api<List<Map<String, Object>>>> getPageToPageList(
            @RequestParam("page_id") String pageId,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        List<Map<String, Object>> pageToPageList = pageToPageService.getPageToPageList(pageId);
        Api<List<Map<String, Object>>> response = Api.<List<Map<String, Object>>>builder()
                .resultCode("200")
                .resultMessage("페이지 목록 조회 성공")
                .data(pageToPageList)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deletePageToPage(
            @RequestParam("path") String path,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        pageToPageService.deletePageToPage(path);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllPageToPageByPageId(
            @RequestParam("page_id") String pageId,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }

        pageToPageService.deleteAllPageToPageByPageId(pageId);
        return ResponseEntity.noContent().build();
    }
}
