package com.example.picknwhip_be.domain.review.controller;

import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.service.command.ReviewCommandService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
  private final ReviewCommandService reviewCommandService;

  @Operation(summary = "리뷰 작성 by 슝/하승연", description = "로그인한 회원이 리뷰를 등록하는 기능입니다.")
  @PostMapping("/{orderId}")
  public ApiResponse<ReviewResDTO.WriteDTO> CreateReview(
      @PathVariable Long orderId,
      @RequestBody @Valid ReviewReqDTO.WriteDTO dto,
      @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.CREATED, reviewCommandService.createReview(orderId, dto, userId));
  }

  @Operation(summary = "리뷰 작성 by 슝/하승연", description = "회원이 작성한 리뷰를 삭제하는 기능입니다.")
  @DeleteMapping("/{reviewId}")
  public ApiResponse<Void> DeleteReview(
      @PathVariable Long reviewId, @Parameter(hidden = true) @ExtractPayload Long userId) {

    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewCommandService.deleteReview(reviewId, userId));
  }
}
