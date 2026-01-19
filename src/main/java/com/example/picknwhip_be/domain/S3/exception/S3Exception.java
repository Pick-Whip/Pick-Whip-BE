package com.example.picknwhip_be.domain.S3.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class S3Exception extends GeneralException {
  public S3Exception(BaseErrorCode code) {
    super(code);
  }
}
