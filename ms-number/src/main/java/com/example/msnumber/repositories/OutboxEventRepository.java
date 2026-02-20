package com.example.msnumber.repositories;

import com.example.msnumber.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop100ByStatusInOrderByCreatedAtAsc(List<String> statuses);
}
