package com.example.picknwhip_be.domain.order.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "orders")
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shop_cake_size_id", nullable = false)
  private ShopCakeSize shopCakeSize;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "design_id")
  private DesignGallery designGallery;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;

  @Column(name = "pickup_datetime", nullable = false)
  private LocalDateTime pickupDatetime;

  @Column(name = "lettering_text", nullable = false, length = 30)
  private String letteringText;

  @Enumerated(EnumType.STRING)
  @Column(name = "lettering_line_count")
  private LetteringLineCount letteringLineCount;

  @Enumerated(EnumType.STRING)
  @Column(name = "lettering_alignment", nullable = false)
  private LetteringAlignment letteringAlignment;

  @Column(name = "additional_request")
  private String additionalRequest;

  @Column(name = "reference_image_url")
  private String referenceImageUrl;

  @Column(name = "payment_method", nullable = false)
  private String paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status", nullable = false)
  private PaymentStatus paymentStatus;

  @Column(name = "total_price", nullable = false)
  private int totalPrice;

  @Column(name = "deposit_amount")
  private int depositAmount;

  @Column(name = "rejection_reason")
  private String rejectionReason;
}
