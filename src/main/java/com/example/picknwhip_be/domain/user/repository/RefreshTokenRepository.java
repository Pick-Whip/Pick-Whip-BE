package com.example.picknwhip_be.domain.user.repository;

import com.example.picknwhip_be.domain.user.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByUserId(Long userId);

  void deleteByUserId(Long userId);
}
