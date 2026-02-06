package com.example.mspackage.service.impl;

import com.example.mspackage.entity.ExceptionMessage;
import com.example.mspackage.enums.ExceptionCode;
import com.example.mspackage.repositories.ExceptionMessageRepository;
import com.example.mspackage.service.ExceptionMessageService;
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