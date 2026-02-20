package com.example.mspackage.repositories;

import com.example.mspackage.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {
    Optional<Package> findFirstByIsDefault(Integer isDefault);

    @Modifying
    @Query("update Package p set p.isDefault = 0 where p.isDefault = 1")
    void clearDefaultFlags();
}
