package com.example.picknwhip_be.domain.review.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class ReviewException extends GeneralException {
  public ReviewException(BaseErrorCode code) {
    super(code);
  }
}
