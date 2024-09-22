package com.example.backend_notion_api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimePatternValidator implements ConstraintValidator<LocalDateTimePattern, LocalDateTime> {

    private String pattern;

    @Override
    public void initialize(LocalDateTimePattern constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern(); // 어노테이션에서 전달된 패턴을 저장
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null은 다른 @NotNull 어노테이션으로 처리
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String formatted = value.format(formatter); // 패턴에 맞게 변환 시도
            LocalDateTime.parse(formatted, formatter);  // 형식에 맞는지 다시 확인
            return true;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false; // 형식이 맞지 않는 경우 유효성 검사 실패
        }
    }
}