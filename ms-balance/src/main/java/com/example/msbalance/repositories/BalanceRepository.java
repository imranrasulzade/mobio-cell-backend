package com.example.msbalance.repositories;

import com.example.msbalance.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    boolean existsByPhoneNumberId(Integer phoneNumberId);
    Optional<Balance> findByPhoneNumberId(Integer phoneNumberId);
    long deleteByPhoneNumberId(Integer phoneNumberId);
}
