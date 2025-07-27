package com.example.msbilling.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trans_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private Integer value;
}
