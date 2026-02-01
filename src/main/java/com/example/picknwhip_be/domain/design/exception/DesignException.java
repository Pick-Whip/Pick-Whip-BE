package com.example.picknwhip_be.domain.design.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class DesignException extends GeneralException {
  public DesignException(BaseErrorCode code) {
    super(code);
  }
}
