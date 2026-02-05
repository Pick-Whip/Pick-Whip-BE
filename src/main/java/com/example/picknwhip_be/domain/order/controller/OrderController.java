package com.example.picknwhip_be.domain.order.controller;

import com.example.picknwhip_be.domain.order.cursor.CursorResult;
import com.example.picknwhip_be.domain.order.dto.req.DraftPickupTimeReqDTO;
import com.example.picknwhip_be.domain.order.dto.req.OrderCursorReqDTO;
import com.example.picknwhip_be.domain.order.dto.req.OrderReqDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderHistoryResDTO;
import com.example.picknwhip_be.domain.order.dto.res.OrderResDTO;
import com.example.picknwhip_be.domain.order.exception.code.OrderSuccessCode;
import com.example.picknwhip_be.domain.order.service.command.OrderCommandService;
import com.example.picknwhip_be.domain.order.service.command.OrderDraftCommandService;
import com.example.picknwhip_be.domain.order.service.query.OrderQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order API", description = "주문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderQueryService orderQueryService;
  private final OrderCommandService orderCommandService;
  private final OrderDraftCommandService orderDraftCommandService;

  @Operation(summary = "주문 생성하기", description = "작성된 주문서(Draft)를 바탕으로 실제 주문을 생성합니다.")
  @PostMapping("")
  public ApiResponse<OrderResDTO.OrderCompleteDTO> createOrder(
      @Parameter(hidden = true) @ExtractPayload Long userId,
      @Valid @RequestBody OrderReqDTO.CreateOrderDTO dto) {

    OrderResDTO.OrderCompleteDTO result = orderCommandService.createOrder(userId, dto);
    return ApiResponse.of(GeneralSuccessCode.CREATED, result);
  }

  @Operation(
      summary = "주문 내역 조회",
      description = "주문 내역을 조회하기 위한 API입니다. type=REQUEST(요청) 또는 COMPLETE(완료)를 선택하세요.")
  @GetMapping("/history")
  public ApiResponse<CursorResult<OrderHistoryResDTO>> getOrderHistory(
      @Parameter(hidden = true) @ExtractPayload Long userId,
      @ParameterObject @ModelAttribute OrderCursorReqDTO reqDto) {
    CursorResult<OrderHistoryResDTO> result = orderQueryService.getOrderHistory(userId, reqDto);
    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }

  @Operation(summary = "주문서 픽업 시간 변경", description = "캘린더에서 시간을 선택 후 확인을 눌렀을 때 호출합니다.")
  @PatchMapping("/drafts/{draftId}/pickup-time")
  public ApiResponse<String> updatePickupTime(
      @ExtractPayload Long userId,
      @PathVariable Long draftId,
      @RequestBody DraftPickupTimeReqDTO dto) {

    orderDraftCommandService.updatePickupTime(userId, draftId, dto.getPickupDatetime());
    return ApiResponse.of(OrderSuccessCode.PICKUP_UPDATED, "픽업 시간이 변경되었습니다.");
  }
}
