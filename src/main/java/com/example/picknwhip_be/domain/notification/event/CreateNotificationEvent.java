package com.example.picknwhip_be.domain.notification.event;

import com.example.picknwhip_be.domain.notification.enums.NotificationKind;

public record CreateNotificationEvent(
    Long userId, NotificationKind kind, Long targetId, String storeName) {}
