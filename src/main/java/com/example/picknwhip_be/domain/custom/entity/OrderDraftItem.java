package com.example.picknwhip_be.domain.custom.entity;

import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "order_draft_items")
public class OrderDraftItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "draft_id", nullable = false)
  private OrderDraft orderDraft;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "custom_option_id", nullable = false)
  private CustomOption customOption;

  @Column(name = "position_x")
  private Double positionX;

  @Column(name = "position_y")
  private Double positionY;
}
