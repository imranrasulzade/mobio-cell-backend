package com.example.msauth.repositories;

import com.example.msauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByPhone(String phone);

    Optional<User> findByPhone(String phone);
}
