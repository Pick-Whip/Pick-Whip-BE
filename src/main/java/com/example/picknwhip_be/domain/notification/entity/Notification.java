package com.example.picknwhip_be.domain.notification.entity;

import com.example.picknwhip_be.domain.notification.enums.NotificationKind;
import com.example.picknwhip_be.domain.notification.enums.NotificationType;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(
    name = "notification",
    indexes = {
      @Index(name = "idx_notification_user_id_id", columnList = "user_id, id"),
      @Index(name = "idx_notification_user_id_type_id", columnList = "user_id, type, id")
    })
public class Notification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Column(name = "kind", nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationKind kind;

  @Column(name = "target_id")
  private Long targetId;

  @Column(name = "title", length = 200, nullable = false)
  private String title;

  @Column(name = "content", length = 200, nullable = false)
  private String content;

  @Column(name = "is_read", nullable = false)
  @Builder.Default
  private boolean isRead = false;

  public void markAsRead() {
    if (!this.isRead) {
      this.isRead = true;
    }
  }
}
