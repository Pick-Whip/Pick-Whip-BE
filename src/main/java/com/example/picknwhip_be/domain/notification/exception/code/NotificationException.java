package com.example.picknwhip_be.domain.notification.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class NotificationException extends GeneralException {
  public NotificationException(BaseErrorCode code) {
    super(code);
  }
}
