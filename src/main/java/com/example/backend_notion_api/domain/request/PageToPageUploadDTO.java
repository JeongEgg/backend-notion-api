package com.example.backend_notion_api.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageToPageUploadDTO {

    private String path;
    private String title;
    private String content;
}
