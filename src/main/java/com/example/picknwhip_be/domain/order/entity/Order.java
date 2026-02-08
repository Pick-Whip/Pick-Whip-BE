package com.example.picknwhip_be.domain.order.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.exception.code.OrderErrorCode;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
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

  @Column(name = "customer_name", nullable = false)
  private String customerName;

  @Column(name = "customer_phone", nullable = false)
  private String customerPhone;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shop_cake_size_id", nullable = false)
  private ShopCakeSize shopCakeSize;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "design_id", nullable = true)
  private DesignGallery designGallery;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;

  @Column(name = "pickup_datetime", nullable = false)
  private LocalDateTime pickupDatetime;

  @Column(name = "lettering_text", length = 30)
  private String letteringText;

  @Enumerated(EnumType.STRING)
  @Column(name = "lettering_line_count")
  private LetteringLineCount letteringLineCount;

  @Enumerated(EnumType.STRING)
  @Column(name = "lettering_alignment")
  private LetteringAlignment letteringAlignment;

  @Column(name = "additional_request")
  private String additionalRequest;

  @Column(name = "order_additional_request")
  private String orderAdditionalRequest;

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
  private Integer depositAmount;

  @Column(name = "rejection_reason")
  private String rejectionReason;

  @Column(name = "order_code", unique = true, length = 20)
  private String orderCode;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @OrderBy("id ASC")
  @Builder.Default
  private Set<OrderItem> orderItems = new LinkedHashSet<>();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @OrderBy("createdAt ASC")
  @Builder.Default
  private Set<OrderHistory> histories = new LinkedHashSet<>();

  public void changeStatus(Status newStatus) {
    if (this.status == Status.COMPLETED
        || this.status == Status.CANCELED_BY_SHOP
        || this.status == Status.PAYMENT_FAILED) {
      throw new OrderException(OrderErrorCode.CANNOT_CHANGE_FINISHED_ORDER);
    }
    this.status = newStatus;
  }

  public void reject(String reason) {
    if (this.status != Status.CONFIRM_WAIT) {
      throw new OrderException(OrderErrorCode.INVALID_ORDER_STATUS);
    }
    this.status = Status.CANCELED_BY_SHOP;
    this.rejectionReason = reason;
  }

  private void validatePaymentProcessable() {
    if (this.status != Status.PAYMENT_WAIT && this.status != Status.PAYMENT_FAILED) {
      throw new OrderException(OrderErrorCode.INVALID_ORDER_STATUS);
    }
  }

  public void accept() {
    if (this.status != Status.CONFIRM_WAIT) {
      throw new OrderException(OrderErrorCode.INVALID_ORDER_STATUS);
    }
    this.status = Status.PAYMENT_WAIT;
  }

  public void paymentFail() {
    validatePaymentProcessable();
    this.status = Status.PAYMENT_FAILED;
    this.paymentStatus = PaymentStatus.WAITING;
  }

  public void paymentSuccess() {
    validatePaymentProcessable();
    this.status = Status.PROD_CONFIRM;
    this.paymentStatus = PaymentStatus.PAID;
  }
}
