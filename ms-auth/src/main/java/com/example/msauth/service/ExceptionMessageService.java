package com.example.msauth.service;

import com.example.msauth.enums.ExceptionCode;

public interface ExceptionMessageService {
    String getLocalizedMessage(ExceptionCode code, String lang);
}
