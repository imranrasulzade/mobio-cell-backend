package com.example.msuser.exception;

import com.example.msuser.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ExceptionCode code) {
        super(code, HttpStatus.NOT_FOUND);
    }
}
