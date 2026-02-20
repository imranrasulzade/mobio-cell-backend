package com.example.mspackage.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackageRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "validityDays is required")
    @Positive(message = "validityDays must be positive")
    private Integer validityDays;

    @NotNull(message = "minuteRate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "minuteRate must be greater than 0")
    private BigDecimal minuteRate;

    private Integer isDefault;
}
