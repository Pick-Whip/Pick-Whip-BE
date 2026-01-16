package com.example.picknwhip_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  // 카카오 로그인 ID (식별용)
  @Column(name = "kakao_id", unique = true, nullable = false)
  private Long kakaoId;

  private String name;

  private String nickname;

  // 테스트용 생성자
  public User(Long kakaoId, String name, String nickname) {
    this.kakaoId = kakaoId;
    this.name = name;
    this.nickname = nickname;
  }
}
