package com.example.picknwhip_be.domain.home.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "popular_design_rankings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PopularDesignRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer ranking; // 1 ~ 4위

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "design_id", nullable = false)
    private DesignGallery design;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "order_count", nullable = false)
    private Long orderCount;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt; // 집계 일시
}