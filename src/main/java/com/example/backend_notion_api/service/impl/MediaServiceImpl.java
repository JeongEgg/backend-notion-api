package com.example.backend_notion_api.service.impl;

import com.example.backend_notion_api.domain.IdUrlDTO;
import com.example.backend_notion_api.s3.ObjectKeyPathBuilder;
import com.example.backend_notion_api.s3.ObjectKeyPathBuilder.*;
import com.example.backend_notion_api.s3.S3Service;
import com.example.backend_notion_api.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService{

    private final S3Service s3Service;


    public MediaServiceImpl(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Override
    public IdUrlDTO uploadFile(String pageId, MultipartFile mediaFile){
        // 업로드
        String fileId = UUID.randomUUID().toString();

        /** 경로 : userId/pageId/Icon/fileId
         *        userId/pageId/Background/fileId
         *        userId/pageId/content/fileId
         *
         *  위의 방식보다는 간단하게 pageId/fileId 형태로 keyname을 사용하는게 더 좋다.
         *  이 방식을 채택하여도 기본적으로 pageId가 겹치지 않기 때문에 데이터의 중복성에 대한 문제가 없다.
         *  장점
         *  1. 간단하기 때문에 데이터를 조회할 때, 클라이언트로부터 요구하는 keyname에 대한 데이터 양이 작다.
         *  2. 데이터 조회속도가 더 빠르다.
         *
         *  단점
         *  1. db를 관리하는 개발자의 입장에서 페이지가 많아질 경우, 직관적으로 찾기가 불편할 수 있다.
         *     (아이콘, 배경이미지, 컨텐츠의 미디어 파일이 구분되지 않음)
         */
        String keyName = ObjectKeyPathBuilder.buildObjectKey(pageId,fileId);
        s3Service.uploadFileWithKeyname(mediaFile,keyName);

        // 다운로드
        String url = s3Service.downloadFileAsURL(keyName);
        IdUrlDTO idUrlDTO = IdUrlDTO.builder()
                .fileId(fileId)
                .fileUrl(url)
                .build();
        return idUrlDTO;
    }
}
