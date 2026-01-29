package com.example.picknwhip_be.domain.home.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HomeSuccessCode {
    POPULAR_CAKES_TOP5_OK(HttpStatus.OK, "HOME_200_1", "인기 케이크 Top5 조회 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    HomeSuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}