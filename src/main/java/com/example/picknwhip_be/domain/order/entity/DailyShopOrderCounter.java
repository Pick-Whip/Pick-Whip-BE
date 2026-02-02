package com.example.picknwhip_be.domain.order.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(
    name = "daily_shop_order_counters",
    uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "date"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyShopOrderCounter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "shop_id", nullable = false)
  private Long shopId;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private int count;

  @Builder
  public DailyShopOrderCounter(Long shopId, LocalDate date, int count) {
    this.shopId = shopId;
    this.date = date;
    this.count = count;
  }

  public void increaseCount() {
    this.count++;
  }
}
