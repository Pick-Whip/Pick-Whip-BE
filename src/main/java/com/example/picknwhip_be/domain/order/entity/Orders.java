package com.example.picknwhip_be.domain.order.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
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
public class Orders extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "user_id", nullable = false)
  //    private Users users;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "shop_id", nullable = false)
  //    private Shops shops;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "shop_cake_size_id", nullable = false)
  //    private Shops shops;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "design_id", nullable = false)
  private DesignGallery designGallery;

  @Column(name = "status", nullable = false)
  private Status status;

  @Column(name = "pickup_datetime", nullable = false)
  private LocalDateTime pickupDatetime;

  @Column(name = "lettering_text", nullable = false, length = 30)
  private String letteringText;

  @Column(name = "lettering_line_count", nullable = false)
  private LetteringLineCount letteringLineCount;

  @Column(name = "lettering_alignment", nullable = false)
  private LetteringAlignment letteringAlignment;

  @Column(name = "additional_request")
  private String additionalRequest;

  @Column(name = "reference_image_url")
  private String referenceImageUrl;

  @Column(name = "payment_method", nullable = false)
  private String paymentMethod;

  @Column(name = "payment_status", nullable = false)
  private PaymentStatus paymentStatus;

  @Column(name = "total_price", nullable = false)
  private int totalPrice;

  @Column(name = "deposit_amount")
  private int depositAmount;

  @Column(name = "rejection_reason")
  private String rejectionReason;
}
