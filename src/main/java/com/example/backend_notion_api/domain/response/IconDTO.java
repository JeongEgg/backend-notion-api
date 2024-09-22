package com.example.backend_notion_api.domain.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IconDTO {

    @JsonProperty("page_id")
    private String pageId;

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("icon_url")
    private String iconUrl;
}
