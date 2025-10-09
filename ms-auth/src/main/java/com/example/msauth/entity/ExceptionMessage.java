package com.example.msauth.entity;

import com.example.msauth.enums.ExceptionCode;
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