package com.example.backend_notion_api.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembersDTO {

    @Id
    private Long id;

    @JsonProperty("team_workspace_id")
    private Long teamWorkspaceId;

    @JsonProperty("user_id")
    private String userId;
}
