package com.example.picknwhip_be.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400_1", "잘못된 요청입니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401_1", "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH403_1", "요청이 거부되었습니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404_1", "요청한 리소스를 찾을 수 없습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500_1", "예기치 않은 서버 에러가 발생했습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_1", "사용자를 찾을 수 없습니다."),
  NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER400_1", "이미 존재하는 닉네임입니다."),
  ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "USER409_1", "이미 탈퇴한 사용자입니다."),
  DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "REPORT400_1", "이미 해당 대상을 신고하셨습니다."),
  INVALID_REPORT_TARGET(HttpStatus.BAD_REQUEST, "REPORT400_2", "본인의 게시글이나 자신은 신고할 수 없습니다."),
  TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT404_1", "신고하려는 대상을 찾을 수 없습니다."),
  REPORT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REPORT500_1", "신고 처리 중 서버 오류가 발생했습니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
