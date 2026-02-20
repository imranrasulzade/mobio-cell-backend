package com.example.msbilling.service;

import com.example.msbilling.entity.Transaction;
import com.example.msbilling.model.BalanceChangedPayload;

public interface TransactionService {
    Transaction createFromBalanceEvent(BalanceChangedPayload payload);
}
