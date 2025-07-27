package com.example.msbilling.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trans_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column
    private Integer value;
}
