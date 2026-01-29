package com.example.picknwhip_be.domain.report.exception;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;

public class ReportException extends GeneralException {
  public ReportException(BaseErrorCode code) {
    super(code);
  }
}
