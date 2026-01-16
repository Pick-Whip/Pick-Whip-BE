package com.example.picknwhip_be.domain.shop.entity;

import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "custom_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "custom_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id")
  private Shop shop;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OptionCategory category;

  @Column(nullable = false)
  private String optionName;

  private int additionalPrice;

  @Column(length = 7)
  private String colorRgbCode;

  public CustomOption(Shop shop, OptionCategory category, String optionName, int additionalPrice) {
    this.shop = shop;
    this.category = category;
    this.optionName = optionName;
    this.additionalPrice = additionalPrice;
  }
}
