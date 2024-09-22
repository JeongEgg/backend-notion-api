package com.example.backend_notion_api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareUpdatedDateDTO {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("page_id")
    private Long pageId;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;
}
