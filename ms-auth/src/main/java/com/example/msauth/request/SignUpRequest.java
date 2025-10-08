package com.example.msauth.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequest {
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;
    private String number;
    private Integer userId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer roleId;
}
