package com.example.msauth.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String phone;
    private String password;
}
