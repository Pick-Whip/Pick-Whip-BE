package com.example.picknwhip_be.domain.notification.service.command;

import static com.example.picknwhip_be.domain.notification.enums.NotificationKind.EVENT;

import com.example.picknwhip_be.domain.notification.entity.Notification;
import com.example.picknwhip_be.domain.notification.enums.NotificationKind;
import com.example.picknwhip_be.domain.notification.event.CreateNotificationEvent;
import com.example.picknwhip_be.domain.notification.exception.code.NotificationErrorCode;
import com.example.picknwhip_be.domain.notification.exception.code.NotificationException;
import com.example.picknwhip_be.domain.notification.repository.NotificationRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Transactional
  @Override
  public void saveNotification(CreateNotificationEvent event) {
    validateEvent(event);

    NotificationKind kind = event.kind();
    validateStoreName(kind, event.storeName());

    Long targetId = validateAndResolveTargetId(kind, event.targetId());

    User targetUser =
        userRepository
            .findById(event.userId())
            .orElseThrow(() -> new NotificationException(NotificationErrorCode.USER_NOT_FOUND));

    String title = render(kind.getTitleTemplate(), event.storeName());
    String content = render(kind.getContentTemplate(), event.storeName());

    Notification notification =
        Notification.builder()
            .user(targetUser)
            .type(kind.getType())
            .kind(kind)
            .targetId(targetId)
            .title(title)
            .content(content)
            .build();

    notificationRepository.save(notification);
  }

  private void validateEvent(CreateNotificationEvent event) {
    if (event == null) {
      throw new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION_EVENT);
    }
    if (event.userId() == null) {
      throw new NotificationException(NotificationErrorCode.INVALID_USER_ID);
    }
    if (event.kind() == null) {
      throw new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION_KIND);
    }
  }

  private void validateStoreName(NotificationKind kind, String storeName) {
    if (kind.isRequiredStoreName() && storeName == null) {
      throw new NotificationException(NotificationErrorCode.STORE_NAME_REQUIRED);
    }
    if (!kind.isRequiredStoreName() && storeName != null) {
      throw new NotificationException(NotificationErrorCode.STORE_NAME_NOT_ALLOWED);
    }
  }

  private Long validateAndResolveTargetId(NotificationKind kind, Long targetId) {
    boolean requiresTargetId = (kind == EVENT) ? false : true;

    if (!requiresTargetId) {
      if (targetId != null) {
        throw new NotificationException(NotificationErrorCode.TARGET_ID_NOT_ALLOWED);
      }
      return null;
    }

    if (targetId == null) {
      throw new NotificationException(NotificationErrorCode.TARGET_ID_REQUIRED);
    }
    return targetId;
  }

  private String render(String template, String storeName) {
    if (storeName == null) {
      return template;
    }
    return template.replace("${storeName}", storeName);
  }
}
