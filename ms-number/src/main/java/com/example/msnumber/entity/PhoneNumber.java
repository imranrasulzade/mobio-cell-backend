package com.example.msnumber.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phone_numbers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String number;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_main")
    private Integer isMain = 0;

    @Column
    private Integer status = 1;
}
