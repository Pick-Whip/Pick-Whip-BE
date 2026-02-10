package com.example.picknwhip_be.domain.home.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "popular_cake_rankings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PopularCakeRanking {

  @Id
  @Column(name = "ranking", nullable = false)
  private Integer ranking;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "design_id", nullable = false)
  private DesignGallery design;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @Column(name = "order_count", nullable = false)
  private Long orderCount;

  @Column(name = "window_start")
  private LocalDateTime windowStart;

  @Column(name = "window_end")
  private LocalDateTime windowEnd;

  @Column(name = "calculated_at", nullable = false)
  private LocalDateTime calculatedAt;
}
