package com.example.picknwhip_be.domain.chat.exception;

import com.example.picknwhip_be.domain.chat.exception.code.ChatErrorCode;
import lombok.Getter;

@Getter
public class ChatException extends RuntimeException {

  private final ChatErrorCode code;

  public ChatException(ChatErrorCode code) {
    super(code.getMessage());
    this.code = code;
  }
}
