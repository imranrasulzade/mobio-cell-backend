package com.example.msbalance.repositories;

import com.example.msbalance.entity.BalanceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
    Page<BalanceHistory> findAllByPhoneNumberIdOrderByCreatedAtDesc(Integer phoneNumberId, Pageable pageable);
}
