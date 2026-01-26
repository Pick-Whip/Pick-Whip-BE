package com.example.picknwhip_be.global.apiPayload.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String secretKeyPlain;

  @Value("${jwt.access-token-validity}")
  private long tokenValidityInMilliseconds;

  private SecretKey key;

  @PostConstruct
  protected void init() {
    this.key = Keys.hmacShaKeyFor(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
  }

  // 토큰 생성
  public String createToken(Long userId) {
    Claims claims = Jwts.claims().subject(userId.toString()).build();
    Date now = new Date();
    Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

    return Jwts.builder().claims(claims).issuedAt(now).expiration(validity).signWith(key).compact();
  }

  // 인증 정보 조회
  public Authentication getAuthentication(String token) {
    String userId = getUserIdFromToken(token);
    User principal =
        new User(userId, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
  }

  // 토큰에서 유저 ID 추출
  public String getUserIdFromToken(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }

  // 토큰 유효성 및 만료 기간 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      // 유효하지 않은 토큰일 경우 false 반환
      return false;
    }
  }
}
