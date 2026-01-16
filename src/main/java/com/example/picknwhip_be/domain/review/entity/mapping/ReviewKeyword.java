package com.example.picknwhip_be.domain.review.entity.mapping;

import com.example.picknwhip_be.domain.review.enums.KeywordCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(
    name = "review_keyword",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_review_keyword_category_keyword",
          columnNames = {"category", "keyword"})
    })
@EntityListeners(AuditingEntityListener.class)
public class ReviewKeyword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "category", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private KeywordCategory category;

  @Column(name = "keyword", nullable = false, length = 15)
  private String keyword;
}
