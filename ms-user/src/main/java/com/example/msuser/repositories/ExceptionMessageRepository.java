package com.example.msuser.repositories;

import com.example.msuser.entity.ExceptionMessage;
import com.example.msuser.enums.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExceptionMessageRepository extends JpaRepository<ExceptionMessage, Long> {
    Optional<ExceptionMessage> findByCodeAndLang(ExceptionCode code, String lang);
}
