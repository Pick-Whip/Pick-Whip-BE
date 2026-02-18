package com.example.picknwhip_be.domain.user.repository;

import com.example.picknwhip_be.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByKakaoId(Long kakaoId); // 카카오 ID로 유저 조회

  boolean existsByNickname(String nickname);

  @Modifying
  @Query(
      value =
          "UPDATE users SET deleted_at = NULL, status = 'ACTIVE' WHERE kakao_id = :kakaoId AND deleted_at IS NOT NULL",
      nativeQuery = true)
  int restoreWithdrawnByKakaoId(@Param("kakaoId") Long kakaoId);
}
