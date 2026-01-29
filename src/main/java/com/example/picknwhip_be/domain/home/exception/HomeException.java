package com.example.picknwhip_be.domain.home.exception;

import com.example.picknwhip_be.domain.home.exception.code.HomeErrorCode;
import lombok.Getter;

@Getter
public class HomeException extends RuntimeException {

    private final HomeErrorCode errorCode;

    public HomeException(HomeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}