package com.example.backend_notion_api.mapper;

import com.example.backend_notion_api.domain.entity.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int existsByUsername(String userId);

    int register(UserDTO userDTO);

    UserDTO findByUsername(String username);
}
