package com.example.picknwhip_be.domain.chat.service.query;

import com.example.picknwhip_be.domain.chat.dto.res.ChatResDTO;

public interface ChatQueryService {
  // 채팅방의 모든 메시지 내역 조회
  ChatResDTO.MessageListDTO getMessages(Long roomId, Long cursor, Integer size);

  // 채팅방 목록 조회
  ChatResDTO.ChatRoomListDTO getChatRoomList(
      Long userId, String keyword, Long cursor, Integer size);

  // 안읽은 메시지 실시간 업데이트
  Long getUnreadCount(Long roomId, Long userId);
}
