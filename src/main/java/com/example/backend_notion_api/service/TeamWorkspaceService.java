package com.example.backend_notion_api.service;

import com.example.backend_notion_api.domain.entity.MembersDTO;
import com.example.backend_notion_api.domain.entity.TeamWorkspaceDTO;
import com.example.backend_notion_api.domain.request.CreateTeamWorkspaceDTO;

public interface TeamWorkspaceService {

    TeamWorkspaceDTO createTeamWorkspace(CreateTeamWorkspaceDTO createTeamWorkspaceDTO);

    void deleteTeamWorkspace(String teamWorkspaceId, String masterId);

    MembersDTO addMembers(String teamWorkspaceId, String masterId, String userId);

    void dropMembers(String teamWorkspaceId, String masterId, String userId);

    TeamWorkspaceDTO changeMaster(String teamWorkspaceId, String prevMasterId, String nextMasterId);
}

