package com.example.mspackage.exception;

import com.example.mspackage.enums.ExceptionCode;
import com.example.mspackage.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(ExceptionCode code) {
        super(code, HttpStatus.BAD_REQUEST);
    }
}

