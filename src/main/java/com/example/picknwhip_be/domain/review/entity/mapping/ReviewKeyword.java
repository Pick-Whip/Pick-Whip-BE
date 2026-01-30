package com.example.picknwhip_be.domain.review.entity.mapping;

import com.example.picknwhip_be.domain.review.enums.KeywordCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "review_keyword")
public class ReviewKeyword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "category", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private KeywordCategory category;

  @Column(name = "code", nullable = false, length = 50, unique = true)
  private String code;

  @Column(name = "label", nullable = false, length = 15)
  private String label;
}
