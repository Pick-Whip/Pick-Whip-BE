package com.example.picknwhip_be.domain.shop.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class ShopException extends GeneralException {
  public ShopException(BaseErrorCode code) {
    super(code);
  }
}
