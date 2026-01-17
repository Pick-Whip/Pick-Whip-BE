package com.example.picknwhip_be.domain.custom.entity;

import com.example.picknwhip_be.domain.order.entity.enums.OptionCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "custom_options")
public class CustomOptions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "shop_id", nullable = false)
  //    private Shops shops;

  @Column(name = "category", nullable = false)
  private OptionCategory category;

  @Column(name = "option_name")
  private String optionName;

  @Column(name = "additional_price")
  private int additionalPrice;

  @Column(name = "color_rgb_code", nullable = false, length = 7)
  private String colorRgbCode;
}
