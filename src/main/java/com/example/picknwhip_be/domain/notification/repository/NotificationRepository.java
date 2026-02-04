package com.example.picknwhip_be.domain.notification.repository;

import com.example.picknwhip_be.domain.notification.entity.Notification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository
    extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
  Long countByUserUserIdAndIsReadFalse(Long userId);

  Optional<Notification> findByIdAndUserUserId(Long id, Long userId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
    update Notification n
       set n.isRead = true
     where n.user.userId = :userId
       and n.isRead = false
""")
  int markAllAsRead(@Param("userId") Long userId);
}
