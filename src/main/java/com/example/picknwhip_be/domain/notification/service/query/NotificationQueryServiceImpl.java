package com.example.picknwhip_be.domain.notification.service.query;

import com.example.picknwhip_be.domain.notification.converter.NotificationConverter;
import com.example.picknwhip_be.domain.notification.dto.res.NotiResDTO;
import com.example.picknwhip_be.domain.notification.entity.Notification;
import com.example.picknwhip_be.domain.notification.enums.NotificationType;
import com.example.picknwhip_be.domain.notification.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService {
  private final NotificationRepository notificationRepository;

  @Override
  public NotiResDTO.NotiListDTO getNotificationList(
      NotificationType type, Long cursor, int size, Long userId) {
    int limit = size + 1;
    List<Notification> rows =
        notificationRepository.searchNotifications(userId, type, cursor, limit);

    boolean hasNext = rows.size() > size;
    List<Notification> items = hasNext ? rows.subList(0, size) : rows;
    Long nextCursor = items.isEmpty() ? null : items.getLast().getId();

    return NotificationConverter.toNotiListDTO(items, nextCursor, hasNext);
  }

  @Override
  public NotiResDTO.UnreadDTO searchUnreadCount(Long userId) {
    Long count = notificationRepository.countByUserUserIdAndIsReadFalse(userId);
    return NotiResDTO.UnreadDTO.builder().unread(count).build();
  }
}
