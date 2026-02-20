package com.example.msbalance.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsumeMinutesRequest {
    @NotNull(message = "minutes is required")
    @Min(value = 1, message = "minutes must be greater than 0")
    private Integer minutes;
    private String description;
}
