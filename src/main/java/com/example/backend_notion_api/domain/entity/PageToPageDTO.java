package com.example.backend_notion_api.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageToPageDTO {

    @JsonProperty("page_to_page_id")
    private Long pageToPageId;

    private String path;

    private String title;

    private String content;
}

