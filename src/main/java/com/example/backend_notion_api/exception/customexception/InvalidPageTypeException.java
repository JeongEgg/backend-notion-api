package com.example.backend_notion_api.exception.customexception;

public class InvalidPageTypeException extends RuntimeException {
    public InvalidPageTypeException(String message) {
        super(message);
    }
}
