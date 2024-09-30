package com.example.backend_notion_api.mapper;

import com.example.backend_notion_api.domain.entity.TeamWorkspaceDTO;

public interface TeamWorkspaceMapper {

    void insertTeamWorkspace(TeamWorkspaceDTO teamWorkspaceDTO);

    void deleteTeamWorkspace(String teamWorkspaceId, String masterId);

    int checkMasterId(String teamWorkspaceId, String masterId);

    void insertMembers(String teamWorkspaceId, String userId);

    void deleteMembers(String teamWorkspaceId, String userId);

    TeamWorkspaceDTO selectTeamWorkspaceById(String teamWorkspaceId);

    void updateMasterId(String teamWorkspaceId, String nextMasterId);
}
