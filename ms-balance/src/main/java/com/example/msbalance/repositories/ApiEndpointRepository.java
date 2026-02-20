package com.example.msbalance.repositories;

import com.example.msbalance.entity.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, Long> {
}
