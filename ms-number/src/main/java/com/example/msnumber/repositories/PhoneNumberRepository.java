package com.example.msnumber.repositories;

import com.example.msnumber.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Boolean existsByNumber(String number);
    Optional<PhoneNumber> findByNumber(String number);
    List<PhoneNumber> findAllByUserIdAndStatusOrderByIsMainDesc(Long userId, Integer status);
    Optional<PhoneNumber> findByUserIdAndIsMain(Long userId, Integer main);
    Long deleteByUserId(Long userId);
}
