package com.example.picknwhip_be.domain.custom.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class CustomException extends GeneralException {
  public CustomException(BaseErrorCode code) {
    super(code);
  }
}
