package com.example.backend_notion_api.exception.customexception;

public class PageToPageNotFoundException extends RuntimeException {
    public PageToPageNotFoundException(String message) {
        super(message);
    }
}
