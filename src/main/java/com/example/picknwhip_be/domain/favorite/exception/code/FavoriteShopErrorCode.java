package com.example.picknwhip_be.domain.favorite.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FavoriteShopErrorCode implements BaseErrorCode {
    FAVORITE_ALREADY_EXISTS(HttpStatus.CONFLICT, "FAVORITE409_1", "이미 마이픽에 등록된 가게입니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "FAVORITE404_1", "마이픽에 등록되지 않은 가게입니다."),
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOP404_1", "존재하지 않는 가게입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_1", "존재하지 않는 사용자입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}