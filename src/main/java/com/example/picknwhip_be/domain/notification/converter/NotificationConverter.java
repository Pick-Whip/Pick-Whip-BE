package com.example.picknwhip_be.domain.notification.converter;

import com.example.picknwhip_be.domain.notification.dto.res.NotiResDTO;
import com.example.picknwhip_be.domain.notification.entity.Notification;
import java.util.List;

public class NotificationConverter {
  public static NotiResDTO.NotiListDTO toNotiListDTO(
      List<Notification> notifications, Long nextCursor, boolean hasNext) {
    List<NotiResDTO.NotiDTO> items =
        notifications.stream().map(NotificationConverter::toNotiDTO).toList();

    return NotiResDTO.NotiListDTO.builder()
        .notifications(items)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }

  public static NotiResDTO.NotiDTO toNotiDTO(Notification notification) {
    return NotiResDTO.NotiDTO.builder()
        .notificationId(notification.getId())
        .title(notification.getTitle())
        .content(notification.getContent())
        .createdDate(notification.getCreatedAt())
        .isRead(notification.isRead())
        .build();
  }
}
