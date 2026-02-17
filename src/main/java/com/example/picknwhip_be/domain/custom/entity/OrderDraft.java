package com.example.picknwhip_be.domain.custom.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "orders_drafts")
public class OrderDraft extends BaseEntity {

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "design_id")
  private DesignGallery designGallery;

  @Column(name = "pickup_datetime")
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

  @Column(name = "reference_image_url")
  private String referenceImageUrl;

  @Column(name = "lettering_color")
  private String letteringColor;

  @Builder.Default
  @OneToMany(mappedBy = "orderDraft", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderDraftItem> items = new ArrayList<>();

  public Long calculateProgress() {
    long progress = 0;

    if (this.shopCakeSize != null) {
      progress += 25;
    }
    if (this.pickupDatetime != null) {
      progress += 25;
    }
    if (this.items != null && !this.items.isEmpty()) {
      progress += 25;
    }
    if (this.letteringText != null) {
      progress += 15;
    }

    return progress;
  }

  public String calculateStatus() {

    if (calculateProgress() == 0) {
      return "EMPTY";
    } else if (calculateProgress() == 25) {
      return "STEP1";
    } else if (calculateProgress() == 50) {
      return "STEP2";
    } else if (calculateProgress() == 75) {
      return "STEP3";
    } else {
      return "COMPLETED";
    }
  }

  public void updatePickupDatetime(LocalDateTime pickupDatetime) {
    this.pickupDatetime = pickupDatetime;
  }
}
