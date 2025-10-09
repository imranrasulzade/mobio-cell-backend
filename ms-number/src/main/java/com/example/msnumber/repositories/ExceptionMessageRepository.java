package com.example.msnumber.repositories;

import com.example.msnumber.entity.ExceptionMessage;
import com.example.msnumber.enums.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExceptionMessageRepository extends JpaRepository<ExceptionMessage, Long> {
    Optional<ExceptionMessage> findByCodeAndLang(ExceptionCode code, String lang);
}
