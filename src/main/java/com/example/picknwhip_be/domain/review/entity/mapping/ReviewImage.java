package com.example.picknwhip_be.domain.review.entity.mapping;

import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "review_image")
public class ReviewImage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;

  @Column(name = "s3_key", nullable = false, length = 512)
  private String s3Key;

  @Column(name = "sort_order", nullable = false)
  private int sortOrder;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public void softDelete(LocalDateTime now) {
    if (this.deletedAt == null) {
      this.deletedAt = now;
    }
  }
}
