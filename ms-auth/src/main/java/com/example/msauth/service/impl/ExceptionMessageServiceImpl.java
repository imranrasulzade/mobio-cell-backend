package com.example.msauth.service.impl;

import com.example.msauth.entity.ExceptionMessage;
import com.example.msauth.enums.ExceptionCode;
import com.example.msauth.repositories.ExceptionMessageRepository;
import com.example.msauth.service.ExceptionMessageService;
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