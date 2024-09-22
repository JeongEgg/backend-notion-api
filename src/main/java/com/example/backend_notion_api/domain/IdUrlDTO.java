package com.example.backend_notion_api.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdUrlDTO {
    private String fileId;
    private String fileUrl;
}
