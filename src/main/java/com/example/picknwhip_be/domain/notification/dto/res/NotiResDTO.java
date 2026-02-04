package com.example.picknwhip_be.domain.notification.dto.res;

import com.example.picknwhip_be.domain.notification.enums.NotificationKind;
import com.example.picknwhip_be.domain.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class NotiResDTO {
  @Builder
  public record NotiListDTO(
      @Schema(description = "알림 목록") List<NotiDTO> notifications,
      @Schema(description = "다음 커서 값", example = "0") Long nextCursor,
      @Schema(description = "다음 데이터 존재", example = "true") boolean hasNext) {}

  @Builder
  public record NotiDTO(
      @Schema(description = "알림 ID", example = "1") Long notificationId,
      @Schema(description = "알림 타입", example = "ORDER") NotificationType type,
      @Schema(description = "알림 종류", example = "ORDER_SHEET_CHECKING") NotificationKind kind,
      @Schema(description = "관련 ID(orderId, reviewId, reportId)", example = "1") Long targetId,
      @Schema(description = "제목", example = "주문서가 전달되었습니다") String title,
      @Schema(description = "본문", example = "가게에서 주문서를 확인 후 알려드릴게요!") String content,
      @Schema(description = "발송일", example = "2026-01-01")
          @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
          LocalDateTime createdDate,
      @Schema(description = "읽음 여부", example = "false") boolean isRead) {}

  @Builder
  public record UnreadDTO(@Schema(description = "알림 안 읽음 개수", example = "1") Long unread) {}

  @Builder
  public record readDTO(@Schema(description = "읽은 알림 ID", example = "1") Long notificationId) {}
}
