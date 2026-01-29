package com.example.picknwhip_be.domain.S3.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class S3CustomException extends GeneralException {
  public S3CustomException(BaseErrorCode code) {
    super(code);
  }
}
