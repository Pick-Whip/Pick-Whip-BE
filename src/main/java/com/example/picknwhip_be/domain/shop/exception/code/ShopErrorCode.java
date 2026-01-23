package com.example.picknwhip_be.domain.shop.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ShopErrorCode implements BaseErrorCode {
  SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOP404_1", "해당 가게를 찾을 수 없습니다."),
  INVALID_RADIUS(HttpStatus.BAD_REQUEST, "SHOP400_1", "반경(radius)은 0보다 크고 3000m 이하여야 합니다."),
  INVALID_COORDINATE(HttpStatus.BAD_REQUEST, "SHOP400_2", "좌표(lat/lon) 값이 올바르지 않습니다."),
  SHOP_NEARBY_QUERY_FAILED(
      HttpStatus.INTERNAL_SERVER_ERROR, "SHOP500_3", "주변 가게 조회 중 서버 오류가 발생했습니다."),

  CAKE_SIZE_NOT_FOUND(HttpStatus.NOT_FOUND, "CAKESIZE404_1", "해당 사이즈를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
