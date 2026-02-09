package com.example.picknwhip_be.domain.notification.repository;

import com.example.picknwhip_be.domain.notification.entity.Notification;
import com.example.picknwhip_be.domain.notification.enums.NotificationType;
import java.util.List;

public interface NotificationRepositoryCustom {
  List<Notification> searchNotifications(
      Long userId, NotificationType type, Long cursor, int limit);
}
