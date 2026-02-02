package com.example.picknwhip_be.domain.favorite.controller;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.service.query.DesignQueryService;
import com.example.picknwhip_be.domain.favorite.dto.req.FavoriteDesignResponse;
import com.example.picknwhip_be.domain.favorite.exception.code.FavoriteSuccessCode;
import com.example.picknwhip_be.domain.favorite.service.FavoriteDesignService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/designs")
@RequiredArgsConstructor
@Tag(name = "FavoriteDesign", description = "마이픽 디자인 관련 API")
public class FavoriteDesignController {

  private final FavoriteDesignService favoriteDesignService;
  private final DesignQueryService designQueryService;

  @Operation(summary = "마이픽 디자인 등록", description = "특정 디자인을 마이픽에 추가합니다.")
  @PostMapping("/{designId}/favorite")
  public ApiResponse<FavoriteDesignResponse> addFavoriteDesign(
      @PathVariable Long designId, @ExtractPayload Long userID) {

    FavoriteDesignResponse result = favoriteDesignService.addFavoriteDesign(designId, userID);

    return ApiResponse.of(FavoriteSuccessCode.FAVORITE_CREATED, result);
  }

  @Operation(summary = "마이픽 디자인 취소", description = "마이픽에 등록된 디자인을 삭제합니다.")
  @DeleteMapping("/{designId}/favorite")
  public ApiResponse<FavoriteDesignResponse> removeFavoriteDesign(
      @PathVariable Long designId, @ExtractPayload Long userID) {

    FavoriteDesignResponse result = favoriteDesignService.deleteFavoriteDesign(designId, userID);

    return ApiResponse.of(FavoriteSuccessCode.FAVORITE_DELETED, result);
  }

  @Operation(summary = "마이픽 디자인 목록 조회", description = "마이픽에 등록된 디자인 목록을 조회합니다")
  @GetMapping("/me")
  public ApiResponse<DesignResDTO.GetDesignListDTO> getFavoriteDesignList(
      @ExtractPayload Long userID) {

    DesignResDTO.GetDesignListDTO result = designQueryService.findDesignListByUserId(userID);

    return ApiResponse.of(FavoriteSuccessCode.FAVORITE_DESIGN_LIST_FETCHED, result);
  }
}
