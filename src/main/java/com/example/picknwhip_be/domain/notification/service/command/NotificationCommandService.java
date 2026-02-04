package com.example.picknwhip_be.domain.notification.service.command;

import com.example.picknwhip_be.domain.notification.event.CreateNotificationEvent;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationCommandService {
  @Transactional
  void saveNotification(CreateNotificationEvent event);
}
