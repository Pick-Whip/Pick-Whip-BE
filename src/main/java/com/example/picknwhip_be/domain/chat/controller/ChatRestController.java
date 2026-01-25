package com.example.picknwhip_be.domain.chat.controller;

import com.example.picknwhip_be.domain.S3.dto.req.S3ReqDTO;
import com.example.picknwhip_be.domain.S3.dto.res.S3ResDTO;
import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.chat.dto.req.ChatRequestDTO;
import com.example.picknwhip_be.domain.chat.dto.res.ChatResponseDTO;
import com.example.picknwhip_be.domain.chat.service.ChatCommandService;
import com.example.picknwhip_be.domain.chat.service.ChatQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "1:1 문의 채팅 관련 API")
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatRestController {
  private final ChatCommandService chatCommandService;
  private final ChatQueryService chatQueryService;
  private final S3Service s3Service;

  @Operation(summary = "채팅방 생성 또는 조회 API", description = "기존 채팅방이 있으면 조회하고, 없으면 새로 생성하여 정보를 반환합니다.")
  @PostMapping
  public ApiResponse<ChatResponseDTO.RoomInfo> createRoom(
      @RequestBody ChatRequestDTO.CreateRoom dto) {
    Long userId = 1L; // TODO: Security 연동
    return ApiResponse.of(GeneralSuccessCode.OK, chatCommandService.saveOrCreateRoom(dto, userId));
  }

  @Operation(
      summary = "채팅 메시지 내역 조회 (페이징) API",
      description =
          "커서 기반 페이징을 사용하여 메시지 내역을 조회합니다. 특정 채팅방의 메시지를 과거 순으로 불러오며, 상대방이 보낸 메시지는 읽음 처리됩니다.")
  @GetMapping("/{roomId}/messages")
  public ApiResponse<ChatResponseDTO.MessageListDTO> getMessages(
      @PathVariable Long roomId,
      @RequestParam(required = false) Long cursor, // 이전 페이지의 마지막 메시지 ID
      @RequestParam(defaultValue = "10") Integer size // 한 번에 가져올 메시지 개수
      ) {
    Long userId = 1L; // TODO: Security 연동
    chatCommandService.updateMarkAsRead(roomId, userId);
    return ApiResponse.of(
        GeneralSuccessCode.OK, chatQueryService.getMessages(roomId, cursor, size));
  }

  // S3 이미지 업로드
  @Operation(
      summary = "채팅방 이미지 전송 URL 발급 API",
      description = "참여자 권한 확인 후 Presigned URL 리스트를 반환합니다.")
  @PostMapping("/{roomId}/images")
  public ApiResponse<List<S3ResDTO.PresignResponseDTO>> createChatImageUrls(
      @PathVariable Long roomId, @RequestBody @Valid S3ReqDTO.BatchDTO request) {

    // TODO: SecurityContext 연동 필요
    Long userId = 1L;

    // 서비스 계층에서 권한 체크 및 URL 생성 수행
    List<S3ResDTO.PresignResponseDTO> result =
        chatCommandService.getChatImageUploadUrls(roomId, userId, request.fileNames());

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }

  @Operation(summary = "내 채팅방 목록 조회 API", description = "로그인한 사용자가 참여 중인 모든 채팅방 목록을 조회합니다.")
  @GetMapping
  public ApiResponse<ChatResponseDTO.ChatRoomListDTO> getChatRoomList() {
    Long userId = 1L; // TODO: Security 연동
    return ApiResponse.of(GeneralSuccessCode.OK, chatQueryService.getChatRoomList(userId));
  }
}
