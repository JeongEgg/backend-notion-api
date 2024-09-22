package com.example.backend_notion_api.service.impl;

import com.example.backend_notion_api.domain.request.JoinDTO;
import com.example.backend_notion_api.domain.entity.UserDTO;
import com.example.backend_notion_api.mapper.UserMapper;
import com.example.backend_notion_api.service.JoinService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JoinServiceImpl implements JoinService {

    @Autowired
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void joinUser(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();

        if (userMapper.existsByUsername(username) >= 1){
            throw new RuntimeException("아이디가 이미 존재함.");
        }
        UserDTO userDTO = new UserDTO().builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                .nickname(joinDTO.getNickname())
                .email(joinDTO.getEmail())
                .role(UserDTO.Role.USER)
                .build();

        userMapper.register(userDTO);
    }
}
