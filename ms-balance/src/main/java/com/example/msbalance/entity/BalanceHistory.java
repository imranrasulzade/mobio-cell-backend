package com.example.msbalance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number_id", nullable = false)
    private Integer phoneNumberId;

    @Column(name = "old_amount", precision = 10, scale = 2)
    private BigDecimal oldAmount;

    @Column(name = "changed_amount", precision = 10, scale = 2)
    private BigDecimal changedAmount;

    @Column(name = "new_amount", precision = 10, scale = 2)
    private BigDecimal newAmount;

    @Column(name = "operation_type", length = 20)
    private String operationType;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
