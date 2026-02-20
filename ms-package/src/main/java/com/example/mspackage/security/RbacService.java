package com.example.mspackage.security;

import com.example.mspackage.repositories.RoleApiPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component("rbacService")
@RequiredArgsConstructor
public class RbacService {

    private final RoleApiPermissionRepository roleApiPermissionRepository;

    public boolean hasAccess(Authentication authentication, String path, String httpMethod) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if (role == null) {
                continue;
            }
            if ("ROLE_INTERNAL".equals(role)) {
                return true;
            }
            if (role.startsWith("ROLE_")) {
                String roleName = role.substring("ROLE_".length());
                if (roleApiPermissionRepository.hasAccess(roleName, path, httpMethod)) {
                    return true;
                }
            }
        }
        return false;
    }
}
