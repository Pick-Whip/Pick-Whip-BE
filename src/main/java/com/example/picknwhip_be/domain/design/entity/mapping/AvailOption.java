package com.example.picknwhip_be.domain.design.entity.mapping;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "avail_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailOption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "avail_option_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "design_id")
  private DesignGallery designGallery;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "custom_option_id")
  private CustomOption customOption;

  @Builder
  public AvailOption(DesignGallery designGallery, CustomOption customOption) {
    this.designGallery = designGallery;
    this.customOption = customOption;
  }
}
