package com.example.mspackage.service;

import com.example.mspackage.enums.ExceptionCode;

public interface ExceptionMessageService {
    String getLocalizedMessage(ExceptionCode code, String lang);
}
