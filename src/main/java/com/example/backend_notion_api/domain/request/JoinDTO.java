package com.example.backend_notion_api.domain.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {

    private String username;
    private String password;
    private String nickname;
    private String email;
}
