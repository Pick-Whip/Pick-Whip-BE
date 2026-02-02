package com.example.picknwhip_be.domain.favorite.entity;

import com.example.picknwhip_be.domain.review.service.user.entity.User;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
    name = "favorite_shops",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_favorite_shop_user",
          columnNames = {"user_id", "shop_id"})
    }) // 한 유저가 같은 가게를 중복 찜하지 못하도록 DB 레벨 제약조건 명시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FavoriteShop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  // 생성자 편의 메서드
  public static FavoriteShop create(User user, Shop shop) {
    return FavoriteShop.builder().user(user).shop(shop).build();
  }
}
