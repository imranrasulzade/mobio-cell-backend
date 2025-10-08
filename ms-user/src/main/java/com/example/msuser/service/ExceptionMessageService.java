package com.example.msuser.service;

import com.example.msuser.enums.ExceptionCode;

public interface ExceptionMessageService {
    String getLocalizedMessage(ExceptionCode code, String lang);
}
