package com.example.backend_notion_api.service;

import com.example.backend_notion_api.domain.CustomUserDetails;
import com.example.backend_notion_api.domain.entity.UserDTO;
import com.example.backend_notion_api.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.findByUsername(username);
        if (userDTO != null) {
            return new CustomUserDetails(userDTO);
        }
        return null;
    }
}
