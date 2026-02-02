package com.example.picknwhip_be.domain.chat.service.command;

import com.example.picknwhip_be.domain.S3.dto.res.S3ResDTO;
import com.example.picknwhip_be.domain.chat.dto.req.ChatReqDTO;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import java.util.List;

public interface ChatCommandService {
  // 채팅방 생성, 기본 방 조회
  ChatResDTO.RoomInfo saveOrCreateRoom(ChatReqDTO.CreateRoom dto, Long customerId);

  // 채팅 메시지 저장 및 권한 검증
  ChatMessage saveMessage(Long roomId, ChatReqDTO.SendMessageDTO dto, Long senderId);

  // 채팅 이미지 업로드 URL 발급
  List<S3ResDTO.PresignResponseDTO> getChatImageUploadUrls(
      Long roomId, Long userId, List<String> fileNames);

  // 채팅방 메시지 읽음 처리
  void updateMarkAsRead(Long roomId, Long userId);
}
