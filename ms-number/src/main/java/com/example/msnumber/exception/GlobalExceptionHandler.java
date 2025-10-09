package com.example.msnumber.exception;

import com.example.msnumber.baseModels.ApiResponse;
import com.example.msnumber.exception.BaseException;
import com.example.msnumber.service.ExceptionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

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
}
