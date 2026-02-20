package com.example.mspackage.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ActiveTariffResponse {
    private Integer numberId;
    private Long packageId;
    private String packageName;
    private BigDecimal minuteRate;
    private LocalDateTime expiresAt;
}
