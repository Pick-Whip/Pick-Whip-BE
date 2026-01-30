package com.example.picknwhip_be.domain.review.controller;

import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.service.command.ReviewCommandService;
import com.example.picknwhip_be.domain.review.service.query.ReviewQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
  private final ReviewCommandService reviewCommandService;
  private final ReviewQueryService reviewQueryService;

  @Operation(summary = "리뷰 작성 by 슝/하승연", description = "로그인한 회원이 리뷰를 등록하는 기능입니다.")
  @PostMapping("/{orderId}")
  public ApiResponse<ReviewResDTO.WriteDTO> createReview(
      @PathVariable Long orderId,
      @RequestBody @Valid ReviewReqDTO.WriteDTO dto,
      @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.CREATED, reviewCommandService.createReview(orderId, dto, userId));
  }

  @Operation(summary = "리뷰 삭제 by 슝/하승연", description = "회원이 작성한 리뷰를 삭제하는 기능입니다.")
  @DeleteMapping("/{reviewId}")
  public ApiResponse<Void> deleteReview(
      @PathVariable Long reviewId, @Parameter(hidden = true) @ExtractPayload Long userId) {

    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewCommandService.deleteReview(reviewId, userId));
  }

  @Operation(summary = "작성한 리뷰 목록 조회 by 슝/하승연", description = "회원이 작성한 리뷰 목록을 조회하는 기능입니다.")
  @GetMapping("/me")
  public ApiResponse<ReviewResDTO.MyReviewListDTO> getMyReviewList(
      @Parameter(description = "커서(마지막으로 조회한 reviewId). 첫 조회는 생략", example = "20")
          @RequestParam(required = false)
          Long cursor,
      @Parameter(description = "조회 개수(기본 20, 최대 50)", example = "20")
          @RequestParam(required = false, defaultValue = "20")
          @Min(1)
          @Max(50)
          int size,
      @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewQueryService.getMyReviewList(cursor, size, userId));
  }
}
