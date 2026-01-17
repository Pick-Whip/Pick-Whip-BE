package com.example.picknwhip_be.domain.payment.entity;

import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.payment.entity.enums.PaymentMethod;
import com.example.picknwhip_be.domain.payment.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(name = "payment_key", unique = true)
  private String paymentKey; // PG사 고유 키

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentMethod paymentMethod;

  @Column(name = "total_amount", nullable = false)
  private long totalAmount;

  @Column(name = "discount_amount")
  @Builder.Default
  private long discountAmount = 0; // 쿠폰 할인액

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private PaymentStatus status;

  @Column(name = "requested_at")
  private LocalDateTime requestedAt;

  @Column(name = "approved_at")
  private LocalDateTime approvedAt;

  @Column(name = "receipt_url")
  private String receiptUrl; // 영수증 URL
}
