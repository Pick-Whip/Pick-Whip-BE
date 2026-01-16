package com.example.picknwhip_be.domain.review.entity.mapping;

import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(
    name = "review_reply",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_review_reply_review",
          columnNames = {"review_id"})
    })
public class ReviewReply extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "review_id", unique = true)
  private Review review;

  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "seller_id")
  //    private User user;

  @Column(name = "content", length = 500, nullable = false)
  private String content;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public void softDelete(LocalDateTime now) {
    if (this.deletedAt == null) {
      this.deletedAt = now;
    }
  }
}
