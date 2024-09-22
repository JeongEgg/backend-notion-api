package com.example.backend_notion_api.domain.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    public enum Role {
        USER, ADMIN
    }

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private Role role;
}
