package com.example.msnumber.entity;

import com.example.msnumber.enums.ExceptionCode;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "exception_messages")
public class ExceptionMessage {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExceptionCode code;
    private String lang;
    private String message;
}