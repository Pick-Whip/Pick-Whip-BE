package com.example.picknwhip_be.domain.home.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HomeErrorCode {
  POPULAR_CAKE_RANKING_NOT_READY(
      HttpStatus.CONFLICT, "HOME_409_1", "인기 케이크 집계가 아직 준비되지 않았습니다. (스케줄러 갱신 전)"),
  INTERNAL_HOME_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "HOME_500_1", "홈 인기 케이크 처리 중 서버 오류가 발생했습니다."),
    POPULAR_DESIGN_RANKING_NOT_READY(HttpStatus.SERVICE_UNAVAILABLE, "HOME_503_2", "인기 디자인 도안 랭킹 데이터가 준비되지 않았습니다.");
  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  HomeErrorCode(HttpStatus httpStatus, String code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }
}
