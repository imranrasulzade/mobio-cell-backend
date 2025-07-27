package com.example.mspackage.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "numbers_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NumbersPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number_id", nullable = false)
    private Integer phoneNumberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package aPackage;

    @Column(name = "activated_at", updatable = false)
    private LocalDateTime activatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "join_at")
    private LocalDateTime joinAt;

    @Column(name = "is_active")
    private Integer isActive = 0;

    @Column(name = "status")
    private Integer status;

    @PrePersist
    public void onCreate() {
        if (activatedAt == null) {
            activatedAt = LocalDateTime.now();
        }
    }
}
