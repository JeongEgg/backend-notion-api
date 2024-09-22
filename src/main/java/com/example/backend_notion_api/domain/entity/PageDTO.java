package com.example.backend_notion_api.domain.entity;

import com.example.backend_notion_api.validator.LocalDateTimePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageDTO {

    @NotNull(message = "페이지 아이디가 null 입니다.")
    @JsonProperty("page_id")
    private Long pageId;

    @NotNull(message = "사용자 아이디가 null 입니다.")
    @JsonProperty("user_id")
    private String userId;

    private String title;

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("background_url")
    private String backgroundUrl;

    @NotNull(message = "업데이트 시간이 null 입니다.")
    @LocalDateTimePattern(message = "업데이트 시간 패턴이 잘못되었습니다. 형식: yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("updated_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    /**
     *  제목1, 제목2, 텍스트 등등.. 의 글자에 대한 마크다운은 단순히 텍스트 앞에
     *  마크다운 태그를 붙여서 그대로 문자열로 받는 것으로 함.
     *
     *  이미지, 오디오, 비디오 등의 파일은 AWS S3 업로드를 먼저 진행한 후,
     *  AWS S3 에서 반환하는 url을 그대로 문자열 타입으로 받는 것으로 함.
     * */
    private String content;

    @JsonProperty("children_page_path_list")
    private List<String> childrenPagePathList;
}
