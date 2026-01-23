package com.example.picknwhip_be.domain.custom.controller;

import com.example.picknwhip_be.domain.custom.dto.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.service.CustomCommandService;
import com.example.picknwhip_be.domain.custom.service.CustomQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Custom API", description = "커스텀 케이크 주문 및 보관함 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customs")
public class CustomController {

  private final CustomQueryService customQueryService;
  private final CustomCommandService customCommandService;

  @Operation(
      summary = "커스텀 케이크 주문(임시저장) 생성",
      description = "사용자가 선택한 옵션으로 커스텀 케이크 주문서(임시저장)를 생성합니다.")
  @PostMapping("")
  public ApiResponse<CustomResDTO.CustomCreateDTO> postCustom(
      @Valid @RequestBody CustomReqDTO.CustomCreateDTO dto) {
    return ApiResponse.of(GeneralSuccessCode.CREATED, customCommandService.saveCustom(dto));
  }

  @Operation(summary = "내 보관함 목록 조회", description = "로그인한 사용자의 임시저장된 커스텀 케이크 목록을 조회합니다.")
  @GetMapping("/drafts")
  public ApiResponse<List<CustomResDTO.GetDraftListDTO>> getDraftList() {

    Long userId = 1L; // 추후 @AuthenticationPrincipal로 교체
    List<CustomResDTO.GetDraftListDTO> result = customQueryService.findDraftList(userId);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }

  @Operation(summary = "보관함 상세 조회", description = "특정 임시저장 주문서(draftId)의 상세 정보를 조회합니다.")
  @GetMapping("/drafts/{draftId}")
  public ApiResponse<CustomResDTO.GetDraftDetailDTO> getDraftDetail(
      @Parameter(description = "삭제할 임시저장 ID", required = true) @PathVariable Long draftId) {
    CustomResDTO.GetDraftDetailDTO result = customQueryService.findDraftDetail(draftId);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }

  @Operation(summary = "보관함 내역 삭제", description = "특정 임시저장 주문서(draftId)를 삭제합니다.")
  @DeleteMapping("/drafts/{draftId}")
  public ApiResponse<CustomResDTO.DeleteDraftDTO> deleteDraft(
      @Parameter(description = "삭제할 임시저장 ID", required = true) @PathVariable Long draftId) {

    Long userId = 1L; // 추후 @AuthenticationPrincipal로 교체

    CustomResDTO.DeleteDraftDTO result = customQueryService.deleteDraft(draftId, userId);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }
}
