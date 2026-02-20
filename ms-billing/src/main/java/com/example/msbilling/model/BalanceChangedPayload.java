package com.example.msbilling.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceChangedPayload {
    private Integer numberId;
    private BigDecimal oldAmount;
    private BigDecimal changedAmount;
    private BigDecimal newAmount;
    private String operationType;
    private String description;
    private Integer minutes;
    private BigDecimal minuteRate;
}
