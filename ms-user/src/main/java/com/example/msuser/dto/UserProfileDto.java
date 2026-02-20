package com.example.msuser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfileDto {
    private Long id;

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @Past(message = "birthDate must be in the past")
    private LocalDate birthDate;

    @Email(message = "email format is invalid")
    private String email;

    @NotNull(message = "roleId is required")
    private Integer roleId;
}
