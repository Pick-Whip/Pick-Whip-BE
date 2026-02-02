package com.example.picknwhip_be.domain.review.service.user.entity;

import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
@SQLDelete(
    sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP, status = 'WITHDRAWN' WHERE user_id = ?")
@Where(clause = "deleted_at is NULL")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(unique = true, nullable = false)
  private Long kakaoId; // 카카오 고유 번호

  @Column(unique = true, nullable = false)
  private String email; // 카카오에서 가져옴

  @Column(nullable = true)
  private String name;

  @Column(unique = true, nullable = false)
  private String nickname;

  @Column(nullable = true)
  private String phone;

  @Column(nullable = true, length = 7)
  private String birthdate;

  @Column(columnDefinition = "TEXT")
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserStatus status = UserStatus.ACTIVE;

  private LocalDateTime deletedAt;

  // 카카오 최초 로그인 시 계정 생성
  public static User createKakaoUser(
      Long kakaoId, String email, String nickname, String profileImageUrl) {
    return User.builder()
        .kakaoId(kakaoId)
        .email(email)
        .nickname(nickname)
        .profileImageUrl(profileImageUrl)
        .status(UserStatus.ACTIVE)
        .build();
  }

  // 카카오 로그인 후 추가 정보 입력
  public void updateExtraInfo(String name, String phone, String birthdate) {
    this.name = name;
    this.phone = phone;
    this.birthdate = birthdate;
  }

  // 마이페이지 수정
  public void updateProfile(String nickname, String profileImageUrl) {
    if (nickname != null && !nickname.isBlank()) {
      this.nickname = nickname;
    }
    if (profileImageUrl != null && !profileImageUrl.isBlank()) {
      this.profileImageUrl = profileImageUrl;
    }
  }
}
