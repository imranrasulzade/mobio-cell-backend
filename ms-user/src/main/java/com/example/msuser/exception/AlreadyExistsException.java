package com.example.msuser.exception;

import com.example.msuser.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BaseException {
    public AlreadyExistsException(ExceptionCode code) {
        super(code, HttpStatus.CONFLICT);
    }
}
