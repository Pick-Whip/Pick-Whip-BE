package com.example.picknwhip_be.domain.notification.repository;

import com.example.picknwhip_be.domain.notification.entity.Notification;
import com.example.picknwhip_be.domain.notification.entity.QNotification;
import com.example.picknwhip_be.domain.notification.enums.NotificationType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Notification> searchNotifications(
      Long userId, NotificationType type, Long cursor, int limit) {
    QNotification n = QNotification.notification;

    BooleanBuilder where = new BooleanBuilder().and(n.user.userId.eq(userId));

    if (type != null) {
      where.and(n.type.eq(type));
    }
    if (cursor != null) {
      where.and(n.id.lt(cursor));
    }

    return queryFactory.selectFrom(n).where(where).orderBy(n.id.desc()).limit(limit).fetch();
  }
}
