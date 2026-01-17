package com.example.picknwhip_be.domain.design.entity.mapping;

import com.example.picknwhip_be.domain.custom.entity.CustomOptions;
import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "orders_options")
public class DesignOptions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "design_id", nullable = false)
  private DesignGallery designGallery;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "custom_id", nullable = false)
  private CustomOptions customOptions;
}
