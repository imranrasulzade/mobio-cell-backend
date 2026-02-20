package com.example.msbalance.repositories;

import com.example.msbalance.entity.RoleApiPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleApiPermissionRepository extends JpaRepository<RoleApiPermission, Long> {
    @Query("""
            select count(rap) > 0
            from RoleApiPermission rap
            where upper(rap.role.name) = upper(:roleName)
              and rap.apiEndpoint.path = :path
              and upper(rap.apiEndpoint.httpMethod) = upper(:httpMethod)
              and rap.apiEndpoint.isDisabled = false
            """)
    boolean hasAccess(@Param("roleName") String roleName,
                      @Param("path") String path,
                      @Param("httpMethod") String httpMethod);
}
