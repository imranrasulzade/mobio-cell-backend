package com.example.msnumber.exception;

import com.example.msnumber.baseModels.ApiResponse;
import com.example.msnumber.exception.BaseException;
import com.example.msnumber.service.ExceptionMessageService;
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

//    @ExceptionHandler(BaseException.class)
//    public ResponseEntity<ApiResponse<?>> handleBaseException(
//            BaseException ex,
//            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang
//    ) {
//        String message = messageService.getLocalizedMessage(ex.getCode(), lang);
//        ApiResponse<?> response = ApiResponse.error(ex.getStatus(), message);
//        return new ResponseEntity<>(response, ex.getStatus());
//    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(
            BaseException ex,
            Locale locale
    ) {
        String lang = (locale != null ? locale.getLanguage() : "az");
        String message = messageService.getLocalizedMessage(ex.getCode(), lang);
        ApiResponse<?> body = ApiResponse.error(ex.getStatus(), message);
        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");
        ApiResponse<?> body = ApiResponse.error(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getMessage())
                .orElse("Validation error");
        ApiResponse<?> body = ApiResponse.error(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAny(Exception ex) {
        log.error("Global exception handler Exception: {}", ex.getMessage(), ex);
        ApiResponse<?> body = ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
