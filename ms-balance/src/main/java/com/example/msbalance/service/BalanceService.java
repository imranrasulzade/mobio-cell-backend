package com.example.msbalance.service;

import com.example.msbalance.entity.Balance;
import com.example.msbalance.repositories.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public void addInitialBalance(Integer numberId) {
        Balance balance = new Balance();
        balance.setPhoneNumberId(numberId);
        balance.setAmount(new BigDecimal("0.00"));
        balanceRepository.save(balance);
        log.info("Added initial balance for numberId={}", numberId);
    }

}
