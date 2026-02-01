package com.example.picknwhip_be.domain.favorite.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FavoriteSuccessCode implements BaseSuccessCode {
  FAVORITE_CREATED(HttpStatus.OK, "FAVORITE200_1", "마이픽에 등록되었습니다."),
  FAVORITE_DELETED(HttpStatus.OK, "FAVORITE200_2", "마이픽 등록이 취소되었습니다."),
  FAVORITE_LIST_FETCHED(HttpStatus.OK, "FAVORITE200_3", "마이픽 가게 목록 조회가 완료되었습니다."),

  FAVORITE_DESIGN_LIST_FETCHED(HttpStatus.OK, "FAVORITE200_4", "마이픽 디자인 목록 조회가 완료되었습니다.");
  private final HttpStatus status;
  private final String code;
  private final String message;
}
