package com.example.backend_notion_api.exception.customexception;

public class JwtExpiredException extends RuntimeException {
  public JwtExpiredException(String message) {
    super(message);
  }
}
