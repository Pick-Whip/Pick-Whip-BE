package com.example.picknwhip_be.domain.user.entity;

import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @Column(nullable = false, unique = true)
  private Long userId;

  @Column(nullable = false)
  private LocalDateTime expiryDate;

  @Builder
  public RefreshToken(String token, Long userId, LocalDateTime expiryDate) {
    this.token = token;
    this.userId = userId;
    this.expiryDate = expiryDate;
  }

  public void updateToken(String newToken, LocalDateTime newExpiryDate) {
    this.token = newToken;
    this.expiryDate = newExpiryDate;
  }
}
