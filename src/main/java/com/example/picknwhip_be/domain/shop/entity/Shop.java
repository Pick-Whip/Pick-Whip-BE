package com.example.picknwhip_be.domain.shop.entity;

import com.example.picknwhip_be.domain.shop.entity.enums.ShopStatus;
import com.example.picknwhip_be.domain.shop.entity.enums.VerificationStatus;
import com.example.picknwhip_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

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
  @JoinColumn(name = "owner_id")
  private User owner;

  @Column(nullable = false)
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
  @Builder.Default
  private VerificationStatus verificationStatus = VerificationStatus.NOT_SUBMITTED;

  @Column(columnDefinition = "TEXT")
  private String pickupTimeGuide;

  @Column(columnDefinition = "TEXT")
  private String dayOrderGuide;

  @Column(columnDefinition = "TEXT")
  private String parkingGuide;

  @Column(columnDefinition = "TEXT")
  private String paymentNotice;
  private Integer prepayment; // 선결제 금액
  @Column(columnDefinition = "TEXT")
  private String precautionNotice;

  private String chatNickname;
  private String chatProfileImageUrl;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ShopStatus status = ShopStatus.HIDDEN;

  public Shop(User owner, String shopName, String phone, Point location) {
    this.owner = owner;
    this.shopName = shopName;
    this.phone = phone;
    this.location = location;
  }
}
