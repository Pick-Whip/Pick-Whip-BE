package com.example.picknwhip_be.domain.user.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class UserException extends GeneralException {
  public UserException(BaseErrorCode code) {
    super(code);
  }
}
