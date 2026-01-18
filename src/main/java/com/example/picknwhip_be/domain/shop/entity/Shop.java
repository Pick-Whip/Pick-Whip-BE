package com.example.picknwhip_be.domain.shop.entity;

import com.example.picknwhip_be.domain.shop.entity.enums.ShopStatus;
import com.example.picknwhip_be.domain.shop.entity.enums.VerificationStatus;
import com.example.picknwhip_be.domain.shop.entity.mapping.ShopKeywordMapping;
import com.example.picknwhip_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shops")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Shop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "shop_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @Column(name = "shop_name", nullable = false)
  private String shopName;

  @Column(nullable = false)
  private String phone;

  @Column(columnDefinition = "POINT SRID 4326", nullable = false)
  private Point location;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "shop_image_url")
  private String shopImageUrl;

  @Column(name = "registration_image_url")
  private String registrationImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "verification_status")
  @Builder.Default
  private VerificationStatus verificationStatus = VerificationStatus.NOT_SUBMITTED;

  @Column(name = "average_rating")
  private Double averageRating;

  @Column(name = "min_price")
  private Integer minPrice;

  @Column(name = "pickup_time_guide", columnDefinition = "TEXT")
  private String pickupTimeGuide;

  @Column(name = "day_order_guide", columnDefinition = "TEXT")
  private String dayOrderGuide;

  @Column(name = "parking_guide", columnDefinition = "TEXT")
  private String parkingGuide;

  @Column(name = "payment_notice", columnDefinition = "TEXT")
  private String paymentNotice;

  private Integer prepayment; // 선결제 금액

  @Column(name = "precaution_notice", columnDefinition = "TEXT")
  private String precautionNotice;

  @Column(name = "chat_nickname")
  private String chatNickname;

  @Column(name = "chat_profile_image_url")
  private String chatProfileImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  @Builder.Default
  private ShopStatus status = ShopStatus.HIDDEN;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
  @Builder.Default
  private List<ShopKeywordMapping> keywordMappings = new ArrayList<>();

  public Shop(User owner, String shopName, String phone, Point location) {
    this.owner = owner;
    this.shopName = shopName;
    this.phone = phone;
    this.location = location;
  }
}
