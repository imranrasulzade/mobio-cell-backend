package com.example.mspackage.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackageResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer validityDays;
    private BigDecimal minuteRate;
    private Integer isDefault;
}
