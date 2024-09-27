package com.example.backend_notion_api.service.impl;

import com.example.backend_notion_api.domain.entity.MembersDTO;
import com.example.backend_notion_api.domain.entity.TeamWorkspaceDTO;
import com.example.backend_notion_api.domain.request.CreateTeamWorkspaceDTO;
import com.example.backend_notion_api.exception.customexception.UserInformationTamperedException;
import com.example.backend_notion_api.mapper.TeamWorkspaceMapper;
import com.example.backend_notion_api.service.TeamWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class TeamWorkspaceServiceImpl implements TeamWorkspaceService {

    @Autowired
    private final TeamWorkspaceMapper teamWorkspaceMapper;

    public TeamWorkspaceServiceImpl(TeamWorkspaceMapper teamWorkspaceMapper) {
        this.teamWorkspaceMapper = teamWorkspaceMapper;
    }

    @Override
    public TeamWorkspaceDTO createTeamWorkspace(CreateTeamWorkspaceDTO createTeamWorkspaceDTO) {
        TeamWorkspaceDTO teamWorkspaceDTO = TeamWorkspaceDTO.builder()
                .teamWorkspaceId(Long.valueOf(createTeamWorkspaceDTO.getMasterId()))
                .masterId(createTeamWorkspaceDTO.getMasterId())
                .title(createTeamWorkspaceDTO.getTitle())
                .iconUrl(createTeamWorkspaceDTO.getIconUrl())
                .comment(createTeamWorkspaceDTO.getComment())
                .updatedDate(LocalDateTime.now())
                .build();

        teamWorkspaceMapper.insertTeamWorkspace(teamWorkspaceDTO);
        return teamWorkspaceDTO;
    }

    @Override
    public void deleteTeamWorkspace(String teamWorkspaceId, String masterId) {
        int masterCheck = teamWorkspaceMapper.checkMasterId(teamWorkspaceId, masterId);
        if (masterCheck == 0) {
            throw new UserInformationTamperedException("팀 워크스페이스의 관리자만 정보를 변경할 수 있습니다.");
        }
        teamWorkspaceMapper.deleteTeamWorkspace(teamWorkspaceId, masterId);
    }

    @Override
    public MembersDTO addMembers(String teamWorkspaceId, String masterId, String userId) {
        int masterCheck = teamWorkspaceMapper.checkMasterId(teamWorkspaceId, masterId);
        if (masterCheck == 0) {
            throw new UserInformationTamperedException("팀 워크스페이스의 관리자만 정보를 변경할 수 있습니다.");
        }

        teamWorkspaceMapper.insertMembers(teamWorkspaceId, userId);
        return MembersDTO.builder()
                .teamWorkspaceId(Long.parseLong(teamWorkspaceId))
                .userId(userId)
                .build();
    }

    @Override
    public void dropMembers(String teamWorkspaceId, String masterId, String userId) {
        int masterCheck = teamWorkspaceMapper.checkMasterId(teamWorkspaceId, masterId);
        if (masterCheck == 0) {
            throw new UserInformationTamperedException("팀 워크스페이스의 관리자만 정보를 변경할 수 있습니다.");
        }

        teamWorkspaceMapper.deleteMembers(teamWorkspaceId, userId);
    }

    @Override
    public TeamWorkspaceDTO changeMaster(String teamWorkspaceId, String prevMasterId, String nextMasterId) {
        int masterCheck = teamWorkspaceMapper.checkMasterId(teamWorkspaceId, prevMasterId);
        if (masterCheck == 0) {
            throw new UserInformationTamperedException("팀 워크스페이스의 관리자만 정보를 변경할 수 있습니다.");
        }

        TeamWorkspaceDTO updatedWorkspace = teamWorkspaceMapper.selectTeamWorkspaceById(teamWorkspaceId);
        updatedWorkspace.setMasterId(nextMasterId);

        teamWorkspaceMapper.updateMasterId(teamWorkspaceId, nextMasterId);
        return updatedWorkspace;
    }
}

