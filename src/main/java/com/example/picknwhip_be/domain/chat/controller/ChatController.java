package com.example.picknwhip_be.domain.chat.controller;

import com.example.picknwhip_be.domain.chat.converter.ChatConverter;
import com.example.picknwhip_be.domain.chat.dto.req.ChatRequestDTO;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResponseDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.service.ChatCommandService;
import com.example.picknwhip_be.domain.chat.service.ChatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
  private final SimpMessagingTemplate messagingTemplate;
  private final ChatCommandService chatCommandService;
  private final ChatQueryService chatQueryService;

  @MessageMapping("api/chats/{roomId}/messages")
  public void sendMessage(@DestinationVariable Long roomId, ChatRequestDTO.SendMessageDTO dto) {
    // TODO: SecurityContext 연동 (임시 1L)
    Long senderId = 1L;

    // 메시지 저장 및 권한 체크
    ChatMessage savedMessage = chatCommandService.saveMessage(roomId, dto, senderId);
    ChatResponseDTO.MessageInfo response = ChatConverter.toMessageInfo(savedMessage);

    // 채팅방 참여자들에게 메시지 실시간 브로드캐스트
    messagingTemplate.convertAndSend("/topic/chats/" + roomId, response);

    // 실시간 안읽은 카운트 알림 처리
    Long customerId = savedMessage.getChatRoom().getCustomer().getUserId();
    Long shopId = savedMessage.getChatRoom().getShop().getId();
    Long shopOwnerId = savedMessage.getChatRoom().getShop().getOwner().getUserId();

    // 수신자 결정 및 카운트 조회
    Long receiverId = senderId.equals(customerId) ? shopOwnerId : customerId;
    Long unreadCount = chatQueryService.getUnreadCount(roomId, receiverId);

    ChatResponseDTO.UnreadCountUpdateDTO unreadUpdate =
        ChatConverter.toUnreadCountUpdateDTO(roomId, unreadCount);

    messagingTemplate.convertAndSend("/topic/users/" + receiverId + "/unread", unreadUpdate);

    if (senderId.equals(customerId)) {
      // 발신자가 고객인 경우 -> 사장님에게 알림 전송
      messagingTemplate.convertAndSend("/topic/shops/" + shopId, "새로운 문의 메시지가 도착했습니다.");
    } else if (senderId.equals(shopOwnerId)) {
      // 발신자가 사장님인 경우 -> 고객에게 알림 전송
      messagingTemplate.convertAndSend("/topic/users/" + customerId, "사장님의 메시지가 도착했습니다.");
    }
  }
}
