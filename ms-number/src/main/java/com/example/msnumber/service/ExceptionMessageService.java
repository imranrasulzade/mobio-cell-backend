package com.example.msnumber.service;

import com.example.msnumber.enums.ExceptionCode;

public interface ExceptionMessageService {
    String getLocalizedMessage(ExceptionCode code, String lang);
}
