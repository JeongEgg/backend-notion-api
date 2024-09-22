package com.example.backend_notion_api.exception.customexception;

public class UserInformationTamperedException extends RuntimeException {
    public UserInformationTamperedException(String message) {
        super(message);
    }
}
