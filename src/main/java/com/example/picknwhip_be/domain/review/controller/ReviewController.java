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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequestMapping("/api/reviews")
@Validated
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

  @Operation(summary = "BEST 커스텀 옵션 조회", description = "도움이 많이 된 리뷰 순으로 커스텀 케이크 정보를 조회합니다. (최대 5개)")
  @GetMapping("/best")
  public ApiResponse<ReviewResDTO.BestReviewListDTO> getBestReviews() {
    return ApiResponse.of(GeneralSuccessCode.OK, reviewQueryService.getBestCustomReviews());
  }

  @Operation(summary = "리뷰 도움 선택 by 슝/하승연", description = "리뷰에 ‘도움이 됐어요’를 선택하는 기능입니다.")
  @PutMapping("/{reviewId}/likes")
  public ApiResponse<ReviewResDTO.ReviewLikeDTO> updateReviewLike(
      @PathVariable Long reviewId, @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewCommandService.saveReviewLike(reviewId, userId));
  }

  @Operation(summary = "리뷰 도움 취소 by 슝/하승연", description = "리뷰에 ‘도움이 됐어요’를 취소하는 기능입니다.")
  @DeleteMapping("/{reviewId}/likes")
  public ApiResponse<ReviewResDTO.ReviewLikeDTO> deleteReviewLike(
      @PathVariable Long reviewId, @Parameter(hidden = true) @ExtractPayload Long userId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewCommandService.deleteReviewLike(reviewId, userId));
  }

  @Operation(summary = "리뷰 상세 조회 by 슝/하승연", description = "특정 리뷰의 정보를 상세 조회하는 기능입니다.")
  @GetMapping("/{reviewId}")
  public ApiResponse<ReviewResDTO.ReviewDetailDTO> getReviewDetail(@PathVariable Long reviewId) {
    return ApiResponse.of(GeneralSuccessCode.OK, reviewQueryService.getReviewDetail(reviewId));
  }

  @Operation(
      summary = "가게 리뷰 통계 조회 by 슝/하승연",
      description = "가게 리뷰 상단에서 평균 별점과 리뷰 개수, 키워드별 순위를 조회하는 기능입니다. ")
  @GetMapping("/{shopId}/summary")
  public ApiResponse<ReviewResDTO.ShopReviewSummaryDTO> getShopReviewSummary(
      @PathVariable Long shopId) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, reviewQueryService.searchShopReviewSummary(shopId));
  }
}
