package com.example.picknwhip_be.domain.notification.entity;

import com.example.picknwhip_be.domain.notification.enums.NotificationType;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "user_id", nullable = false)
  //    private User user;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Column(name = "title", length = 512, nullable = false)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "is_read", nullable = false)
  @Builder.Default
  private boolean isRead = false;
}
