package com.example.msbilling.repositories;

import com.example.msbilling.entity.TransactionSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionSourceRepository extends JpaRepository<TransactionSource, Long> {
    Optional<TransactionSource> findByValue(Integer value);
}
