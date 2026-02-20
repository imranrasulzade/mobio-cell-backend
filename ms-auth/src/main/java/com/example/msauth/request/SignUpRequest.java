package com.example.msauth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class SignUpRequest {
    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^\\+?[0-9]{7,20}$", message = "phone format is invalid")
    private String phone;

    @Email(message = "email format is invalid")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "confirmPassword is required")
    private String confirmPassword;

    private Integer userId;

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @Past(message = "birthDate must be in the past")
    private LocalDate birthDate;

    @NotNull(message = "roleId is required")
    private Integer roleId;
}
