package com.example.backend_notion_api.controller;

import com.example.backend_notion_api.domain.request.JoinDTO;
import com.example.backend_notion_api.domain.response.Api;
import com.example.backend_notion_api.service.impl.JoinServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinServiceImpl joinService;

    public JoinController(JoinServiceImpl joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<Api<String>> join(@RequestBody JoinDTO joinDTO){
        System.out.println("userId: " + joinDTO.getUsername());

        // 사용자 가입 로직 실행
        joinService.joinUser(joinDTO);

        // Api 응답 생성
        Api<String> response = Api.<String>builder()
                .resultCode("200")
                .resultMessage("가입이 성공적으로 완료되었습니다.")
                .data("가입된 사용자: " + joinDTO.getUsername())
                .build();

        // 응답 반환
        return ResponseEntity.ok(response);
    }
}
