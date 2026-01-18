package com.example.picknwhip_be.domain.design.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "design_gallery")
public class DesignGallery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "shop_id", nullable = false)
  //    private Shops shops;

  @Column(name = "design_name", nullable = false)
  private String designName;

  @Column(name = "base_price", nullable = false)
  private int basePrice;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "allergy_info")
  private String allergyInfo;

  @Column(name = "description")
  private String description;
}
