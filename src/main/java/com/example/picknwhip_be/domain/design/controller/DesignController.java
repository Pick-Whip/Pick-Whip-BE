package com.example.picknwhip_be.domain.design.controller;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.service.query.DesignQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  }

  @GetMapping("/{userId}")
  public DesignResDTO.GetDesignListDTO getDesignListByUserId(@PathVariable Long userId) {

    return null;
  }

  @Operation(
      summary = "디자인 갤러리 목록 조회 (메인)",
      description = "카테고리, 정렬 조건과 함께 현재 위치(위도/경도)를 필수로 받아 디자인 목록을 조회합니다.")
  @GetMapping("/gallery")
  public ApiResponse<DesignResDTO.GalleryListDTO> getDesignGallery(
      @Parameter(description = "카테고리") @RequestParam(required = false, defaultValue = "전체")
          String category,
      @Parameter(description = "정렬 기준 (NAME, NEARBY, RATING)")
          @RequestParam(required = false, defaultValue = "NAME")
          String sort,
      @Parameter(description = "현재 위치 위도", required = true) @RequestParam(required = false)
          Double lat,
      @Parameter(description = "현재 위치 경도", required = true) @RequestParam(required = false)
          Double lon,
      @RequestParam(defaultValue = "0") int page,
      @ExtractPayload Long userId) {
    DesignResDTO.GalleryListDTO result =
        designQueryService.searchGallery(category, sort, lat, lon, userId, page);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }
}
