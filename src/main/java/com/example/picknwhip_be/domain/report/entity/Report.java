package com.example.picknwhip_be.domain.report.entity;

import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reportId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reporter_id")
  private User reporter;

  private Long targetId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportTargetType targetType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportReasonType reasonType;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "ENUM('PENDING', 'COMPLETED') DEFAULT 'PENDING'")
  @Builder.Default
  private ReportStatus status = ReportStatus.PENDING;

  @Column(columnDefinition = "TEXT")
  private String answer;

  @Column(name = "processed_at")
  private LocalDateTime processedAt;
}
