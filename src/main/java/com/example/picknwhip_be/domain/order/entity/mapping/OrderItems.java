package com.example.picknwhip_be.domain.order.entity.mapping;

import com.example.picknwhip_be.domain.custom.entity.CustomOptions;
import com.example.picknwhip_be.domain.order.entity.Orders;
import com.example.picknwhip_be.domain.order.entity.enums.OptionCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "order_items")
public class OrderItems {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Orders orders;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "custom_id", nullable = false)
  private CustomOptions customOptions;

  @Enumerated(EnumType.STRING)
  @Column(name = "option_category", nullable = false)
  private OptionCategory optionCategory;

  @Column(name = "option_name", nullable = false)
  private String optionName;

  @Column(name = "unit_price", nullable = false)
  private int unitPrice;

  @Column(name = "color_rgb_code", nullable = false, length = 7)
  private String colorRgbCode;
}
