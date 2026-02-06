package com.example.mspackage.repositories;

import com.example.mspackage.entity.ExceptionMessage;
import com.example.mspackage.enums.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExceptionMessageRepository extends JpaRepository<ExceptionMessage, Long> {
    Optional<ExceptionMessage> findByCodeAndLang(ExceptionCode code, String lang);
}
