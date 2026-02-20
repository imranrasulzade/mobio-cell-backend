package com.example.msbalance.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BalanceDetailsResponse {
    private Integer numberId;
    private BigDecimal amount;
    private LocalDateTime lastUpdated;
}
