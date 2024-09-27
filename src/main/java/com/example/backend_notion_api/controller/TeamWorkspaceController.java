package com.example.backend_notion_api.controller;

import com.example.backend_notion_api.domain.entity.MembersDTO;
import com.example.backend_notion_api.domain.entity.TeamWorkspaceDTO;
import com.example.backend_notion_api.domain.request.CreateTeamWorkspaceDTO;
import com.example.backend_notion_api.domain.response.Api;
import com.example.backend_notion_api.exception.customexception.UserNotFoundException;
import com.example.backend_notion_api.service.impl.TeamWorkspaceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team-workspaces")
public class TeamWorkspaceController {

    @Autowired
    private final TeamWorkspaceServiceImpl teamWorkspaceService;

    public TeamWorkspaceController(TeamWorkspaceServiceImpl teamWorkspaceService) {
        this.teamWorkspaceService = teamWorkspaceService;
    }

    @PostMapping("/create")
    public ResponseEntity<Api<TeamWorkspaceDTO>> createTeamWorkspace(
            @Valid @RequestBody CreateTeamWorkspaceDTO createTeamWorkspaceDTO,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        String masterId = authentication.getName();

        // 팀 워크스페이스 생성
        createTeamWorkspaceDTO.setMasterId(masterId);
        TeamWorkspaceDTO teamWorkspaceDTO = teamWorkspaceService.createTeamWorkspace(createTeamWorkspaceDTO);

        // 성공 응답 생성
        Api<TeamWorkspaceDTO> response = Api.<TeamWorkspaceDTO>builder()
                .resultCode("200")
                .resultMessage("팀 워크스페이스가 성공적으로 생성되었습니다.")
                .data(teamWorkspaceDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Api<String>> deleteTeamWorkspace(
            @RequestParam("team_workspace_id") String teamWorkspaceId,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        String masterId = authentication.getName();

        // 팀 워크스페이스 삭제
        teamWorkspaceService.deleteTeamWorkspace(teamWorkspaceId, masterId);

        Api<String> response = Api.<String>builder()
                .resultCode("200")
                .resultMessage("팀 워크스페이스가 성공적으로 삭제되었습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-member")
    public ResponseEntity<Api<MembersDTO>> addMembers(
            @RequestParam("team_workspace_id") String teamWorkspaceId,
            @RequestParam("user_id") String userId,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        String masterId = authentication.getName();

        // 팀 멤버 추가
        MembersDTO membersDTO = teamWorkspaceService.addMembers(teamWorkspaceId, masterId, userId);

        Api<MembersDTO> response = Api.<MembersDTO>builder()
                .resultCode("200")
                .resultMessage("팀 멤버가 성공적으로 추가되었습니다.")
                .data(membersDTO)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/drop-member")
    public ResponseEntity<Api<String>> dropMembers(
            @RequestParam("team_workspace_id") String teamWorkspaceId,
            @RequestParam("user_id") String userId,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        String masterId = authentication.getName();

        // 팀 멤버 삭제
        teamWorkspaceService.dropMembers(teamWorkspaceId, masterId, userId);

        Api<String> response = Api.<String>builder()
                .resultCode("200")
                .resultMessage("팀 멤버가 성공적으로 삭제되었습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-master")
    public ResponseEntity<Api<TeamWorkspaceDTO>> changeMaster(
            @RequestParam("team_workspace_id") String teamWorkspaceId,
            @RequestParam("next_master_id") String nextMasterId,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("일치하는 아이디가 존재하지 않습니다.");
        }
        String prevMasterId = authentication.getName();

        // 마스터 변경
        TeamWorkspaceDTO updatedWorkspace = teamWorkspaceService.changeMaster(teamWorkspaceId, prevMasterId, nextMasterId);

        Api<TeamWorkspaceDTO> response = Api.<TeamWorkspaceDTO>builder()
                .resultCode("200")
                .resultMessage("팀 워크스페이스 관리자가 성공적으로 변경되었습니다.")
                .data(updatedWorkspace)
                .build();

        return ResponseEntity.ok(response);
    }
}
