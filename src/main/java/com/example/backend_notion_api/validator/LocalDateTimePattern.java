package com.example.backend_notion_api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocalDateTimePatternValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateTimePattern {

    String message() default "잘못된 날짜 형식입니다. 형식: yyyy-MM-dd'T'HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern() default "yyyy-MM-dd'T'HH:mm:ss"; // 기본 패턴
}