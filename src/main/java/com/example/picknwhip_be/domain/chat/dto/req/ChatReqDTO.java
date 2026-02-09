package com.example.picknwhip_be.domain.chat.dto.req;

import com.example.picknwhip_be.domain.chat.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatReqDTO {
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateRoom {
    private Long shopId;
    private Long orderId;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SendMessageDTO {
    private MessageType type;
    private String content;
    private String imageUrl;
  }
}
