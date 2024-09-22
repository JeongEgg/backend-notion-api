package com.example.backend_notion_api.exception;

import com.example.backend_notion_api.domain.response.Api;
import com.example.backend_notion_api.exception.customexception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<Api<String>> handleJwtExpiredException(JwtExpiredException ex) {
        Api<String> response = Api.<String>builder()
                .resultCode("401")
                .resultMessage("토큰이 만료됨 : "+ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Api<String>> handlePageNotFoundException(PageNotFoundException ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("PAGE_NOT_FOUND")
                .resultMessage("페이지를 찾을 수 없습니다: " + ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Api<String>> handleUserNotFoundException(UserNotFoundException ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("USER_NOT_FOUND")
                .resultMessage(ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserInformationTamperedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Api<String>> handleUserInformationTamperedException(UserInformationTamperedException ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("USER_INFO_TAMPERED")
                .resultMessage(ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPageTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Api<String>> handleInvalidPageTypeException(InvalidPageTypeException ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("INVALID_PAGE_TYPE")
                .resultMessage("잘못된 페이지 타입: " + ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.apache.tomcat.util.http.fileupload.FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Api<String>> handleFileUploadException(org.apache.tomcat.util.http.fileupload.FileUploadException ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("FILE_UPLOAD_ERROR")
                .resultMessage("파일 업로드 실패 : " + ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Api<String>> handleFileDownloadException(FileDownloadException ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("FILE_DOWNLOAD_ERROR")
                .resultMessage("파일 다운로드 실패 : " + ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        Api<List<String>> response = Api.<List<String>>builder()
                .resultCode("400")
                .resultMessage("validation 오류")
                .data(errorMessages)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Api<List<String>>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = "업데이트 시간 형식이 잘못되었습니다. 형식: yyyy-MM-dd'T'HH:mm:ss";

        Api<List<String>> response = Api.<List<String>>builder()
                .resultCode("400")
                .resultMessage("validation 오류")
                .data(Collections.singletonList(errorMessage)) // 리스트 형태로 담기
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(PageToPageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Api<String>> handlePageToPageNotFoundException(PageToPageNotFoundException ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("PAGE_TO_PAGE_NOT_FOUND")
                .resultMessage("존재하지 않는 페이지에 대한 요청 : [path : "+ex.getMessage()+"]")
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Api<String>> handleGeneralException(Exception ex) {
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("GENERAL_ERROR")
                .resultMessage("예기치 않은 에러 발생 : " + ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

