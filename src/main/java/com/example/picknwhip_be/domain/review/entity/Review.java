package com.example.picknwhip_be.domain.review.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewLike;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "review")
@SQLRestriction("deleted_at is null")
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false, unique = true)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "design_id")
  private DesignGallery design;

  @Column(name = "rating", nullable = false)
  private Integer rating;

  @Column(name = "content", length = 500, nullable = false)
  private String content;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
  private List<ReviewLike> likes = new ArrayList<>();

  @Column(name = "agreement")
  @Builder.Default
  private Boolean agreement = true;

  public void softDelete(LocalDateTime now) {
    if (this.deletedAt == null) {
      this.deletedAt = now;
    }
  }

  public boolean isDeleted() {
    return this.deletedAt != null;
  }
}
