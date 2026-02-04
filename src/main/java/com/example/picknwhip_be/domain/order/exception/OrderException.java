package com.example.picknwhip_be.domain.order.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class OrderException extends GeneralException {
  public OrderException(BaseErrorCode code) {
    super(code);
  }
}
