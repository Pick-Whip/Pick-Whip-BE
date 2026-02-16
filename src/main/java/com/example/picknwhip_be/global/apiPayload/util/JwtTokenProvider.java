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

  @Value("${jwt.refresh-token-validity:1209600000}")
  private long refreshTokenValidityInMilliseconds; // 14일 기본값

  private SecretKey key;

  @PostConstruct
  protected void init() {
    this.key = Keys.hmacShaKeyFor(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
  }

  private static final String TOKEN_TYPE_ACCESS = "access";
  private static final String TOKEN_TYPE_REFRESH = "refresh";

  // 액세스 토큰 생성
  public String createToken(Long userId) {
    Claims claims = Jwts.claims().subject(userId.toString()).add("type", TOKEN_TYPE_ACCESS).build();
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
      Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
      if (!TOKEN_TYPE_ACCESS.equals(claims.get("type", String.class))) {
        throw new com.example.picknwhip_be.global.apiPayload.exception.GeneralException(
            com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode.INVALID_TOKEN);
      }
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      throw new com.example.picknwhip_be.global.apiPayload.exception.GeneralException(
          com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode.INVALID_TOKEN);
    } catch (ExpiredJwtException e) {
      throw new com.example.picknwhip_be.global.apiPayload.exception.GeneralException(
          com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode.TOKEN_EXPIRED);
    } catch (UnsupportedJwtException e) {
      throw new com.example.picknwhip_be.global.apiPayload.exception.GeneralException(
          com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode.INVALID_TOKEN);
    } catch (IllegalArgumentException e) {
      throw new com.example.picknwhip_be.global.apiPayload.exception.GeneralException(
          com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode.INVALID_TOKEN);
    }
  }

  // 리프레시 토큰 생성
  public String createRefreshToken(Long userId) {
    Claims claims =
        Jwts.claims().subject(String.valueOf(userId)).add("type", TOKEN_TYPE_REFRESH).build();
    Date now = new Date();
    Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

    return Jwts.builder().claims(claims).issuedAt(now).expiration(validity).signWith(key).compact();
  }

  public Claims getClaimsFromExpiredToken(String token) {
    try {
      return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    } catch (io.jsonwebtoken.security.SecurityException
        | MalformedJwtException
        | UnsupportedJwtException
        | IllegalArgumentException e) {
      throw new com.example.picknwhip_be.global.apiPayload.exception.GeneralException(
          com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode.INVALID_TOKEN);
    }
  }
}
