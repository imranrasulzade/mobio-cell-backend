package com.example.mspackage.exception;

import com.example.mspackage.enums.ExceptionCode;
import com.example.mspackage.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ExceptionCode code) {
        super(code, HttpStatus.NOT_FOUND);
    }
}
