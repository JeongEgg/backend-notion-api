package com.example.backend_notion_api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class TeamWorkspaceDTO {

    @JsonProperty("team_workspace_id")
    private Long teamWorkspaceId;

    @JsonProperty("master_id")
    private String masterId;

    private String title;

    @JsonProperty("icon_url")
    private String iconUrl;

    private String comment;

    @JsonProperty("updated_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;
}

