package com.example.picknwhip_be.domain.order.entity.mapping;

import com.example.picknwhip_be.domain.custom.entity.CustomOptions;
import com.example.picknwhip_be.domain.order.entity.OrderDrafts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "order_draft_items")
public class OrderDraftItems {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "draft_id", nullable = false)
  private OrderDrafts orderDrafts;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "custom_id", nullable = false)
  private CustomOptions customOptions;
}
