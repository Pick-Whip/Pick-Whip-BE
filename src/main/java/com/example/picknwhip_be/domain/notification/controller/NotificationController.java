package com.example.picknwhip_be.domain.notification.controller;

import com.example.picknwhip_be.domain.notification.dto.res.NotiResDTO;
import com.example.picknwhip_be.domain.notification.enums.NotificationType;
import com.example.picknwhip_be.domain.notification.service.query.NotificationQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequestMapping("/api/notifications")
@Validated
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationQueryService notificationQueryService;

  @Operation(summary = "알림 목록 조회 by 슝/하승연", description = "알림 화면에서 알림 목록을 조회하는 기능입니다.")
  @GetMapping("")
  public ApiResponse<NotiResDTO.NotiListDTO> getNotificationList(
      @Parameter(description = "조회하려는 탭(디폴트 값은 전체 조회)", example = "ORDER")
          @RequestParam(required = false)
          NotificationType notificationType,
      @Parameter(description = "커서(마지막으로 조회한 notificationId). 첫 조회는 생략", example = "20")
          @RequestParam(required = false)
          Long cursor,
      @Parameter(description = "조회 개수(기본 20, 최대 50)", example = "20")
          @RequestParam(required = false, defaultValue = "20")
          @Min(1)
          @Max(50)
          int size,
      @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK,
        notificationQueryService.getNotificationList(notificationType, cursor, size, userId));
  }

  @Operation(summary = "알림 안 읽음 개수 조회 by 슝/하승연", description = "알림 화면에서 안 읽음 알림 개수를 조회하는 기능입니다. ")
  @GetMapping("/unread")
  public ApiResponse<NotiResDTO.UnreadDTO> getUnreadCount(
      @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, notificationQueryService.searchUnreadCount(userId));
  }
}
