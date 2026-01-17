package com.example.picknwhip_be.domain.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_cake_sizes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ShopCakeSize {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "size_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id")
  private Shop shop;

  @Column(nullable = false)
  private String sizeName; // 예: "1호"

  @Column(nullable = false)
  private String diameter; // 예: "15cm"

  @Column(nullable = false)
  private int price; // 기본 가격

  public ShopCakeSize(Shop shop, String sizeName, String diameter, int price) {
    this.shop = shop;
    this.sizeName = sizeName;
    this.diameter = diameter;
    this.price = price;
  }
}
