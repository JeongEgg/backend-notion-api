package com.example.backend_notion_api.controller;


import com.example.backend_notion_api.domain.IdUrlDTO;
import com.example.backend_notion_api.domain.response.Api;
import com.example.backend_notion_api.domain.response.FileDTO;
import com.example.backend_notion_api.service.impl.MediaServiceImpl;
import com.example.backend_notion_api.service.impl.PageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/medias")
public class MediaController {

    private final MediaServiceImpl mediaService;
    private final PageServiceImpl pageService;

    public MediaController(MediaServiceImpl mediaService, PageServiceImpl pageService) {
        this.mediaService = mediaService;
        this.pageService = pageService;
    }

    @PostMapping("/icon")
    public ResponseEntity<Api<FileDTO>> uploadIcon(
            @RequestParam("page_id") String pageId,
            @RequestPart("icon_file")MultipartFile iconFile,
            Authentication authentication
    ){
        String username = authentication.getName();
        return handleFileUpload(pageId, iconFile,username,"파일이 업로드 되었습니다.");
    }

    @PostMapping("/image")
    public ResponseEntity<Api<FileDTO>> uploadBackgroundImage(
            @RequestParam("page_id") String pageId,
            @RequestPart("background_file")MultipartFile backgroundImageFile,
            Authentication authentication
    ){
        String username = authentication.getName();
        return handleFileUpload(pageId, backgroundImageFile,username,"파일이 업로드 되었습니다.");
    }

    /**
     *  컨텐츠 부분에서 미디어 파일을 업로드한 경우, 파일의 UUID 와 URL 을 반환하고
     *  페이지가 수정될 때, 다시 UUID, URL 을 업로드하여 저장한다.
     *
     *  1. UUID 를 반환하는 이유는 페이지 내에서 특정 미디어 타입의 파일을 삭제할 때,
     *     UUID 를 사용하여 데이터를 S3 저장소에서 조회하고 삭제하기 위함이다.
     *
     *  2. URL 을 반환하는 이유는 페이지에 대한 데이터를 DB 에서 조회할 때마다, S3 에서도 다시
     *     데이터를 조회하도록 하지 않고, 단순히 URL 만 반환하여 프론트에서 사용 가능하도록 하기
     *     위함이다.
     * */
    @PostMapping("/content")
    public ResponseEntity<Api<FileDTO>> uploadContentFile(
            @RequestParam("page_id") String pageId,
            @RequestPart("content_file")MultipartFile contentMediaFile,
            Authentication authentication
    ){
        String username = authentication.getName();
        return handleFileUpload(pageId, contentMediaFile,username,"파일이 업로드 되었습니다.");
    }

    private ResponseEntity<Api<FileDTO>> handleFileUpload(
            String pageId,
            MultipartFile file,
            String username,
            String successMessage
    ){
        pageService.countPageByIdAndUserId(pageId, username);

        IdUrlDTO idUrlDTO = mediaService.uploadFile(pageId, file);
        FileDTO iconResponseDTO = FileDTO.builder()
                .pageId(pageId)
                .fileId(idUrlDTO.getFileId())
                .fileUrl(idUrlDTO.getFileUrl())
                .build();

        Api<FileDTO> response = Api.<FileDTO>builder()
                .resultCode("200")
                .resultMessage(successMessage)
                .data(iconResponseDTO)
                .build();

        return ResponseEntity.ok(response);
    }

}