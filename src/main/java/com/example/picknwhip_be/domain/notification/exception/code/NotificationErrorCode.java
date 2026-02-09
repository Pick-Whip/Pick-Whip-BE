package com.example.picknwhip_be.domain.notification.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements BaseErrorCode {
  INVALID_NOTIFICATION_KIND(HttpStatus.BAD_REQUEST, "NOTIFICATION400_1", "알림 종류 정보가 필요합니다."),
  INVALID_NOTIFICATION_EVENT(HttpStatus.BAD_REQUEST, "NOTIFICATION400_2", "이벤트 정보가 필요합니다."),
  INVALID_USER_ID(HttpStatus.BAD_REQUEST, "NOTIFICATION400_3", "타겟 유저 정보가 필요합니다."),
  TARGET_ID_NOT_ALLOWED(HttpStatus.FORBIDDEN, "NOTIFICATION403_1", "EVENT는 타겟 ID가 허용되지 않습니다."),
  TARGET_ID_REQUIRED(HttpStatus.FORBIDDEN, "NOTIFICATION403_2", "타겟 ID가 필요한 알림 타입입니다."),
  STORE_NAME_REQUIRED(HttpStatus.FORBIDDEN, "NOTIFICATION403_3", "가게명이 필요한 알림 타입입니다."),
  STORE_NAME_NOT_ALLOWED(HttpStatus.FORBIDDEN, "NOTIFICATION403_4", "가게명이 허용되지 않는 알림 타입입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION404_1", "해당 유저를 찾을 수 없습니다."),
  NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION404_2", "해당 알림을 찾을 수 없습니다.");
  private final HttpStatus status;
  private final String code;
  private final String message;
}
