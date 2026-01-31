package com.example.picknwhip_be.domain.favorite.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class FavoriteException extends GeneralException {
  public FavoriteException(BaseErrorCode code) {
    super(code);
  }
}
