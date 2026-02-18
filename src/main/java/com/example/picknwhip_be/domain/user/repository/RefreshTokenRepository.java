package com.example.picknwhip_be.domain.user.repository;

import com.example.picknwhip_be.domain.user.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByUserId(Long userId);

  @Modifying
  @Query("DELETE FROM RefreshToken r WHERE r.userId = :userId")
  void deleteByUserId(@Param("userId") Long userId);
}
