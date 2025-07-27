package com.example.msbilling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "api_endpoints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String path;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "is_public")
    private boolean isPublic = false;

    @Column(name = "is_disabled")
    private boolean isDisabled = false;

    @OneToMany(mappedBy = "apiEndpoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleApiPermission> rolePermissions;
}
