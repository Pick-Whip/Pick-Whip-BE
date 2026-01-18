package com.example.picknwhip_be.domain.shop.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ShopErrorCode implements BaseErrorCode {
  SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOP404_1", "해당 가게를 찾을 수 없습니다."),

  CAKE_SIZE_NOT_FOUND(HttpStatus.NOT_FOUND, "CAKESIZE404_1", "해당 사이즈를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
