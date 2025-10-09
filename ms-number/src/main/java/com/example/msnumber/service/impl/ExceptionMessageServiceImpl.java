package com.example.msnumber.service.impl;

import com.example.msnumber.entity.ExceptionMessage;
import com.example.msnumber.enums.ExceptionCode;
import com.example.msnumber.repositories.ExceptionMessageRepository;
import com.example.msnumber.service.ExceptionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExceptionMessageServiceImpl implements ExceptionMessageService {

    private final ExceptionMessageRepository repository;

    public String getLocalizedMessage(ExceptionCode code, String lang) {
        return repository.findByCodeAndLang(code, lang)
                .map(ExceptionMessage::getMessage)
                .orElse("Message not found for " + code);
    }
}