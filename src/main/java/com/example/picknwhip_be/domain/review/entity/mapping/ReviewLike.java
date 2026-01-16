package com.example.picknwhip_be.domain.review.entity.mapping;

import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(
//    name = "review_like",
//    uniqueConstraints = {
//      @UniqueConstraint(
//          name = "uk_review_like_review_user",
//          columnNames = {"review_id", "user_id"})
//    }
    )
@EntityListeners(AuditingEntityListener.class)
public class ReviewLike extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;

  //    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "user_id", nullable = false)
  //    private User user;

  private LocalDateTime deletedAt;

  public void softDelete(LocalDateTime now) {
    this.deletedAt = now;
  }
}
