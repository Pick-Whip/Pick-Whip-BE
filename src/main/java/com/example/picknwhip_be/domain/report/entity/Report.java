package com.example.picknwhip_be.domain.report.entity;

import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.global.common.BaseEntity;
import jakarta.persistence.*;
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

  private Long shopId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportTargetType targetType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportReasonType reasonType;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
}
