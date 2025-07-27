package com.example.msnotification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_api_permissions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "api_endpoint_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleApiPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_endpoint_id", nullable = false)
    private ApiEndpoint apiEndpoint;
}
