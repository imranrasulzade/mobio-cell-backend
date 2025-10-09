package com.example.msauth.exception;

import com.example.msauth.enums.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ExceptionCode code;
    private final HttpStatus status;

    public BaseException(ExceptionCode code, HttpStatus status) {
        super(code.name());
        this.code = code;
        this.status = status;
    }
}
