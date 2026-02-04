package com.example.picknwhip_be.domain.notification.repository;

import com.example.picknwhip_be.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
    extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {}
