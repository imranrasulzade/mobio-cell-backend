package com.example.msauth.exception;

import com.example.msauth.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(ExceptionCode code) {
        super(code, HttpStatus.BAD_REQUEST);
    }
}

