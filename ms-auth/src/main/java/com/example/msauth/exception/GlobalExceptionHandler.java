package com.example.msauth.exception;

import com.example.msauth.baseModels.ApiResponse;
import com.example.msauth.service.ExceptionMessageService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionMessageService messageService;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(BaseException ex, Locale locale) {
        String lang = resolveLang(locale);
        String message = messageService.getLocalizedMessage(ex.getCode(), lang);
        ApiResponse<?> body = ApiResponse.error(ex.getStatus(), message);
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    // @Valid DTO validation (request body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        ApiResponse<?> body = ApiResponse.error(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(body);
    }

    // @Validated param validation (request param/path/header)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getMessage())
                .orElse("Validation error");

        ApiResponse<?> body = ApiResponse.error(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(body);
    }

    // (null pointer, feign error, db down)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAny(Exception ex) {
        log.error("Global exception handler Exception: {}", ex.getMessage(), ex);
        ApiResponse<?> body = ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private String resolveLang(Locale locale) {
        return (locale != null && locale.getLanguage() != null && !locale.getLanguage().isBlank())
                ? locale.getLanguage()
                : "az";
    }
}
