package com.example.picknwhip_be.domain.chat.service;

import com.example.picknwhip_be.domain.chat.converter.ChatConverter;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResponseDTO;
import com.example.picknwhip_be.domain.chat.entity.ChatMessage;
import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.chat.repository.ChatMessageRepository;
import com.example.picknwhip_be.domain.chat.repository.ChatRoomRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.global.apiPayload.code.GeneralErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryServiceImpl implements ChatQueryService {

  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;

  @Override
  public List<ChatResponseDTO.MessageInfo> findMessages(Long roomId) {
    ChatRoom room =
        chatRoomRepository
            .findById(roomId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

    return chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(room).stream()
        .map(ChatConverter::toMessageInfo)
        .toList();
  }

  @Override
  public ChatResponseDTO.ChatRoomListDTO getChatRoomList(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_FOUND));

    // 유저가 참여 중인 채팅방 조회
    List<ChatRoom> rooms = chatRoomRepository.findAllByCustomerOrShopOwner(user);

    // 각 방별로 요약 정보 생성
    List<ChatResponseDTO.ChatRoomSummaryDTO> summaryList =
        rooms.stream()
            .map(
                room -> {
                  // 마지막 메시지 조회
                  ChatMessage lastMessage =
                      chatMessageRepository
                          .findTopByChatRoomOrderByCreatedAtDesc(room)
                          .orElse(null);

                  // 안읽은 메시지 카운트 (본인이 보낸 건 제외)
                  Long unreadCount =
                      chatMessageRepository.countByChatRoomAndIsReadFalseAndSenderNot(room, user);

                  return ChatConverter.toChatRoomSummaryDTO(room, lastMessage, unreadCount);
                })
            .sorted(
                Comparator.comparing(ChatResponseDTO.ChatRoomSummaryDTO::getLastMessageTime)
                    .reversed()) // 최신순 정렬
            .toList();

    return ChatConverter.toChatRoomListDTO(summaryList);
  }

  @Override
  public Long getUnreadCount(Long roomId, Long userId) {
    ChatRoom room =
        chatRoomRepository
            .findById(roomId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.CHAT_ROOM_NOT_FOUND));
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_FOUND));

    return chatMessageRepository.countByChatRoomAndIsReadFalseAndSenderNot(room, user);
  }
}
