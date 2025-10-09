package com.example.msnumber.exception;

import com.example.msnumber.enums.ExceptionCode;
import com.example.msnumber.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BaseException {
    public AlreadyExistsException(ExceptionCode code) {
        super(code, HttpStatus.CONFLICT);
    }
}
