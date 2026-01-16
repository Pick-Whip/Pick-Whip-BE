package com.example.picknwhip_be.domain.review.entity.mapping;

import com.example.picknwhip_be.domain.review.entity.Review;
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
    name = "review_selected_keyword",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_review_keyword",
          columnNames = {"review_id", "keyword_id"})
    })
@EntityListeners(AuditingEntityListener.class)
public class ReviewSelectedKeyword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "keyword_id", nullable = false)
  private ReviewKeyword keyword;

  private LocalDateTime deletedAt;

  public void softDelete(LocalDateTime now) {
    this.deletedAt = now;
  }
}
