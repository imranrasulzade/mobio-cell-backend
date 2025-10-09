package com.example.msauth.exception;

import com.example.msauth.enums.ExceptionCode;
import com.example.msauth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ExceptionCode code) {
        super(code, HttpStatus.NOT_FOUND);
    }
}
