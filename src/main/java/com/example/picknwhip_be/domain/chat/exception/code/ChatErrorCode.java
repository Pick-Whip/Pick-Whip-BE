package com.example.picknwhip_be.domain.chat.exception.code;

import com.example.picknwhip_be.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT404_1", "존재하지 않는 채팅방입니다."),
    CHAT_NOT_PARTICIPANT(HttpStatus.FORBIDDEN, "CHAT403_1", "해당 채팅방의 참여자가 아닙니다."),
    INVALID_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "CHAT400_1", "잘못된 메시지 형식입니다."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT404_2", "메시지를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}