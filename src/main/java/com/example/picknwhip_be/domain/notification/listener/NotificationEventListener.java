package com.example.picknwhip_be.domain.notification.listener;

import com.example.picknwhip_be.domain.notification.event.CreateNotificationEvent;
import com.example.picknwhip_be.domain.notification.service.command.NotificationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationCommandService notificationCommandService;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(CreateNotificationEvent event) {
    notificationCommandService.saveNotification(event);
  }
}
