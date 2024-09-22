package com.example.backend_notion_api.service;

import com.example.backend_notion_api.domain.request.JoinDTO;

public interface JoinService {
    void joinUser(JoinDTO joinDTO);
}
