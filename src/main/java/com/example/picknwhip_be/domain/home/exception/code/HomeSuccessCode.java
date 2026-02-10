package com.example.picknwhip_be.domain.home.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HomeSuccessCode implements BaseSuccessCode {
  POPULAR_CAKES_TOP5_OK(HttpStatus.OK, "HOME_200_1", "인기 케이크 Top5 조회 성공"),
    POPULAR_DESIGNS_TOP4_OK(HttpStatus.OK, "HOME_200_2", "인기 케이크 도안 Top4 조회 성공");
  private final HttpStatus status;
  private final String code;
  private final String message;
}
