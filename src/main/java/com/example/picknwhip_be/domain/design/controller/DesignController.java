package com.example.picknwhip_be.domain.design.controller;

import com.example.picknwhip_be.domain.design.dto.DesignResDTO;
import com.example.picknwhip_be.domain.design.service.DesignQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Design API", description = "디자인 갤러리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/design")
public class DesignController {

  private final DesignQueryService designQueryService;

  @Operation(summary = "가게 디자인갤러리 조회 API", description = "특정 가게의 디자인갤러리 목록을 조회합니다.")
  @GetMapping("/shop/{shopId}")
  public ApiResponse<DesignResDTO.GetDesignListDTO> getDesignListByShopId(
      @PathVariable Long shopId) {

    DesignResDTO.GetDesignListDTO result = designQueryService.findDesignListByShopId(shopId);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }

  @Operation(summary = "디자인갤러리 상세조회 API", description = "design_id로 디자인 상세정보를 조회합니다.")
  @GetMapping("/{designId}")
  public ApiResponse<DesignResDTO.GetDesignDetailDTO> getDesignDetail(@PathVariable Long designId) {

    DesignResDTO.GetDesignDetailDTO result = designQueryService.findDesignDetail(designId);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  @GetMapping("/{userId}")
  public DesignResDTO.GetDesignListDTO getDesignListByUserId(@PathVariable Long userId) {

    return null;
  }

}
