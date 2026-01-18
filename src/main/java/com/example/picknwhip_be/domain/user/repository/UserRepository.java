package com.example.picknwhip_be.domain.user.repository;

import com.example.picknwhip_be.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByKakaoId(Long kakaoId); // 카카오 ID로 유저 조회

  boolean existsByNickname(String nickname);
}
