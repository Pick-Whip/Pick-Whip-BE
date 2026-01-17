package com.example.picknwhip_be.domain.shop.entity;

import com.example.picknwhip_be.domain.shop.entity.enums.KeywordType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_appeal_keywords")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ShopAppealKeyword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "keyword_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "keyword_type")
  private KeywordType keywordType;

  @Column(name = "keyword_text", nullable = false)
  private String keywordText;
}
