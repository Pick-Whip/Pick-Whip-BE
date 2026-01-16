package com.example.picknwhip_be.domain.review.entity.mapping;

import com.example.picknwhip_be.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

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
}
