package com.example.mspackage.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackageRequest {
    private String name;
    private BigDecimal price;
    private Integer validityDays;
}
