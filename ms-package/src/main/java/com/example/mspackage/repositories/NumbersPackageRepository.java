package com.example.mspackage.repositories;

import com.example.mspackage.entity.NumbersPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NumbersPackageRepository extends JpaRepository<NumbersPackage, Long> {
    @Query("""
            select count(np) > 0
            from NumbersPackage np
            where np.phoneNumberId = :phoneNumberId
              and np.aPackage.id = :packageId
              and np.isActive = :isActive
            """)
    boolean existsActivePackage(@Param("phoneNumberId") Integer phoneNumberId,
                                @Param("packageId") Long packageId,
                                @Param("isActive") Integer isActive);

    @Query("""
            select np
            from NumbersPackage np
            where np.phoneNumberId = :phoneNumberId
              and np.isActive = 1
              and (np.expiresAt is null or np.expiresAt > :now)
            order by np.activatedAt desc
            """)
    Optional<NumbersPackage> findActiveByPhoneNumber(@Param("phoneNumberId") Integer phoneNumberId,
                                                     @Param("now") LocalDateTime now);
}
