package com.example.mspackage.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "validity_days", nullable = false)
    private Integer validityDays;

    @Column(name = "minute_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal minuteRate;

    @Column(name = "is_default", nullable = false)
    private Integer isDefault = 0;
}
