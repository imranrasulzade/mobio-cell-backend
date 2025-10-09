package com.example.msauth.repositories;

import com.example.msauth.entity.ExceptionMessage;
import com.example.msauth.enums.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExceptionMessageRepository extends JpaRepository<ExceptionMessage, Long> {
    Optional<ExceptionMessage> findByCodeAndLang(ExceptionCode code, String lang);
}
