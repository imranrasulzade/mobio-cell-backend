package com.example.msuser.service.impl;

import com.example.msuser.entity.ExceptionMessage;
import com.example.msuser.enums.ExceptionCode;
import com.example.msuser.repositories.ExceptionMessageRepository;
import com.example.msuser.service.ExceptionMessageService;
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