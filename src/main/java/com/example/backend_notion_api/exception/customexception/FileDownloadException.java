package com.example.backend_notion_api.exception.customexception;

public class FileDownloadException extends RuntimeException {
    public FileDownloadException(String message) {
        super(message);
    }
}
