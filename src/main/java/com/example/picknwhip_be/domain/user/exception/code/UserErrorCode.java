package com.example.picknwhip_be.domain.user.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_1", "사용자를 찾을 수 없습니다."),
  NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER400_1", "이미 존재하는 닉네임입니다."),
  ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "USER409_1", "이미 탈퇴한 사용자입니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
