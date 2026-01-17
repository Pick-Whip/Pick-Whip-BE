package com.example.picknwhip_be.domain.test.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class TestException extends GeneralException {
  public TestException(BaseErrorCode code) {
    super(code);
  }
}
