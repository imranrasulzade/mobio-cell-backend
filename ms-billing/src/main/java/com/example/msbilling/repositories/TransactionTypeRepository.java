package com.example.msbilling.repositories;

import com.example.msbilling.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
    Optional<TransactionType> findByValue(Integer value);
}
