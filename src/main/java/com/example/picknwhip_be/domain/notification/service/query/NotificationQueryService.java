package com.example.picknwhip_be.domain.notification.service.query;

import com.example.picknwhip_be.domain.notification.dto.res.NotiResDTO;
import com.example.picknwhip_be.domain.notification.enums.NotificationType;

public interface NotificationQueryService {
  NotiResDTO.NotiListDTO getNotificationList(
      NotificationType type, Long cursor, int size, Long userId);

  NotiResDTO.UnreadDTO searchUnreadCount(Long userId);
}
