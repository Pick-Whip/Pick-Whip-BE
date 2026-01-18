package com.example.picknwhip_be.domain.order.entity;

import com.example.picknwhip_be.domain.order.entity.enums.OptionCategory;
import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order orders;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "custom_id", nullable = false)
  private CustomOption customOptions;

  @Enumerated(EnumType.STRING)
  @Column(name = "option_category", nullable = false)
  private OptionCategory optionCategory;

  @Column(name = "option_name", nullable = false)
  private String optionName;

  @Column(name = "unit_price", nullable = false)
  private int unitPrice;

  @Column(name = "color_rgb_code", nullable = false, length = 7)
  private String colorRgbCode;

  @Column(name = "position_x")
  private Double positionX;

  @Column(name = "position_y")
  private Double positionY;
}
