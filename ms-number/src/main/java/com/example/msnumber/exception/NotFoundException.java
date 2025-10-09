package com.example.msnumber.exception;

import com.example.msnumber.enums.ExceptionCode;
import com.example.msnumber.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ExceptionCode code) {
        super(code, HttpStatus.NOT_FOUND);
    }
}
