package com.example.msauth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {
    @NotBlank(message = "phone is required")
    private String phone;

    @NotBlank(message = "password is required")
    private String password;
}
