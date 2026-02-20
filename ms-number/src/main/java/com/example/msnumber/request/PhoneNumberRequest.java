package com.example.msnumber.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PhoneNumberRequest {
    @NotBlank(message = "number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,20}$", message = "number format is invalid")
    private String number;

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "isMain is required")
    private Integer isMain;

    @NotNull(message = "status is required")
    private Integer status;
}
