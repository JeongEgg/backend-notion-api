package com.example.backend_notion_api.service;

import com.example.backend_notion_api.domain.IdUrlDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaService {

    IdUrlDTO uploadFile(String pageId, MultipartFile mediaFile) throws IOException;

}
