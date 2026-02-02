package com.example.picknwhip_be.domain.design.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DesignErrorCode implements BaseErrorCode {
  DESIGN_NOT_FOUND(HttpStatus.NOT_FOUND, "DESIGN404_1", "해당 디자인을 찾을 수 없습니다."),


  private final HttpStatus status;
  private final String code;
  private final String message;
}
