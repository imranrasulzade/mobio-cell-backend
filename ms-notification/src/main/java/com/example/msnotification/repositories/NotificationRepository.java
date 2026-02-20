package com.example.msnotification.repositories;

import com.example.msnotification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByNumberIdOrderBySentAtDesc(Integer numberId, Pageable pageable);
}
