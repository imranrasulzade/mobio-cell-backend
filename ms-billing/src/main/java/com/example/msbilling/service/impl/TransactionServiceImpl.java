package com.example.msbilling.service.impl;

import com.example.msbilling.entity.Transaction;
import com.example.msbilling.entity.TransactionSource;
import com.example.msbilling.entity.TransactionType;
import com.example.msbilling.model.BalanceChangedPayload;
import com.example.msbilling.repositories.TransactionRepository;
import com.example.msbilling.repositories.TransactionSourceRepository;
import com.example.msbilling.repositories.TransactionTypeRepository;
import com.example.msbilling.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionSourceRepository transactionSourceRepository;
    private final TransactionTypeRepository transactionTypeRepository;

    @Override
    @Transactional
    public Transaction createFromBalanceEvent(BalanceChangedPayload payload) {
        TransactionSource source = transactionSourceRepository.findByValue(1)
                .orElseThrow(() -> new IllegalStateException("Transaction source seed is missing"));
        TransactionType type = resolveType(payload.getOperationType());

        Transaction transaction = Transaction.builder()
                .numberId(payload.getNumberId())
                .amount(payload.getChangedAmount())
                .description(payload.getDescription() != null ? payload.getDescription() : payload.getOperationType())
                .status("SUCCESS")
                .source(source)
                .type(type)
                .build();
        return transactionRepository.save(transaction);
    }

    private TransactionType resolveType(String operationType) {
        int value = switch (operationType) {
            case "TOPUP" -> 1;
            case "MINUTE_CONSUME" -> 2;
            default -> 3;
        };
        return transactionTypeRepository.findByValue(value)
                .orElseThrow(() -> new IllegalStateException("Transaction type seed is missing for value=" + value));
    }
}
