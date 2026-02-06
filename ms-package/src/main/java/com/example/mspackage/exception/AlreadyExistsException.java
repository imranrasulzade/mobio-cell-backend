package com.example.mspackage.exception;

import com.example.mspackage.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BaseException {
    public AlreadyExistsException(ExceptionCode code) {
        super(code, HttpStatus.CONFLICT);
    }
}
