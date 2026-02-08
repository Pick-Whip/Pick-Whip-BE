package com.example.picknwhip_be.domain.review.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class ReviewResDTO {
  // 리뷰 작성 DTO
  @Builder
  public record WriteDTO(@Schema(description = "생성된 리뷰 ID", example = "1") Long reviewId) {}

  // 리뷰 DTO 코어
  @Builder
  public record ReviewDTO(
      @Schema(description = "리뷰 ID", example = "1") Long reviewId,
      @Schema(description = "별점", example = "5") int rating,
      @Schema(description = "리뷰 내용", example = "너무 예쁘고 맛있어요!") String content,
      @Schema(description = "사장님 답글", example = "감사합니다.") String reply,
      @Schema(description = "리뷰 이미지 url 목록", example = "[\"https://....jpg\"]")
          List<String> imageUrls,
      @Schema(description = "작성일", example = "2026-01-01")
          @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
          LocalDateTime createdDate) {}

  // 작성한 리뷰
  @Builder
  public record MyReviewItemDTO(
      @Schema(description = "가게명", example = "달콤한 순간") String shopName,
      @Schema(description = "케이크 옵션", example = "기념일 케이크") String option,
      @Schema(description = "리뷰 내용") ReviewDTO review) {}

  // 작성한 리뷰 목록 조회 DTO
  @Builder
  public record MyReviewListDTO(
      @Schema(description = "작성한 리뷰 개수", example = "1") int count,
      @Schema(description = "평균 별점", example = "5.0") double rating,
      @Schema(description = "리뷰 목록") List<MyReviewItemDTO> items,
      @Schema(description = "다음 커서 값", example = "0") Long nextCursor,
      @Schema(description = "다음 데이터 존재", example = "true") boolean hasNext) {}

  // 리뷰 키워드(code-label)
  public record KeywordDTO(String code, String label) {}

  // 리뷰 상세 조회 DTO
  @Builder
  public record ReviewDetailDTO(
      @Schema(description = "리뷰 ID", example = "1") Long reviewId,
      @Schema(description = "닉네임") String nickname,
      @Schema(description = "프로필 사진 url") String profileUrl,
      @Schema(description = "별점", example = "5") int rating,
      @Schema(description = "리뷰 내용", example = "너무 예쁘고 맛있어요!") String content,
      @Schema(description = "리뷰 이미지 url 목록", example = "[\"https://....jpg\"]")
          List<String> imageUrls,
      @Schema(description = "키워드 목록") List<KeywordDTO> keywords,
      @Schema(description = "작성일", example = "2026-01-01")
          @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
          LocalDateTime createdDate,
      @Schema(description = "사장님 답글", example = "감사합니다.") String reply) {}

  // 리뷰 도움 선택/취소 DTO
  @Builder
  public record ReviewLikeDTO(
      @Schema(description = "리뷰 ID") Long reviewId,
      @Schema(description = "도움이 됐어요 여부") boolean isLike,
      @Schema(description = "도움이 됐어요 수") Long likeCount) {}

  // 가게 리뷰 목록 DTO
  @Builder
  public record ShopReviewListDTO(
      @Schema(description = "리뷰 목록") List<ShopReviewItemDTO> items,
      @Schema(description = "다음 커서 값", example = "0") String nextCursor,
      @Schema(description = "다음 데이터 존재", example = "true") boolean hasNext) {}

  @Builder
  public record ShopReviewItemDTO(
      @Schema(description = "리뷰 ID", example = "1") Long reviewId,
      @Schema(description = "닉네임") String nickname,
      @Schema(description = "프로필 사진 url") String profileUrl,
      @Schema(description = "별점", example = "5") int rating,
      @Schema(description = "케이크 옵션", example = "기념일 케이크") String option,
      @Schema(description = "리뷰 내용", example = "너무 예쁘고 맛있어요!") String content,
      @Schema(description = "리뷰 이미지 url 목록", example = "[\"https://....jpg\"]")
          List<String> imageUrls,
      @Schema(description = "키워드 목록") List<KeywordDTO> keywords,
      @Schema(description = "도움이 됐어요 여부") boolean isLike,
      @Schema(description = "도움이 됐어요 수") Long likeCount,
      @Schema(description = "작성일", example = "2026-01-01")
          @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
          LocalDateTime createdDate) {}

  @Builder
  public record ShopReviewSummaryDTO(
      @Schema(description = "평균 별점", example = "4.8") double rating,
      @Schema(description = "리뷰 개수", example = "342") int count,
      @Schema(description = "키워드 순위") KeywordRankingDTO keywordRanking) {}

  @Builder
  public record KeywordRankingDTO(
      @Schema(description = "디자인만족", example = "50") int designSatisfaction,
      @Schema(description = "결과물통일", example = "35") int sameAsResult,
      @Schema(description = "맛", example = "28") int taste,
      @Schema(description = "소통", example = "17") int communication,
      @Schema(description = "픽업진행", example = "3") int pickup) {}

  // 홈화면 - 베스트 커스텀 옵션 리뷰 조회 DTO
  @Builder
  public record BestReviewListDTO(
      @Schema(description = "베스트 리뷰 목록") List<BestReviewItemDTO> items) {}

  @Builder
  public record BestReviewItemDTO(
      @Schema(description = "작성자 닉네임", example = "해밍") String writerName,
      @Schema(description = "작성일", example = "2025.12.09") String createdDate, // 포맷팅해서 문자열로 반환
      @Schema(description = "별점", example = "5") int rating,
      @Schema(description = "도움이 됐어요 개수", example = "10") Long helpfulCount,
      @Schema(description = "리뷰 내용", example = "생각보다 크기가 딱 좋았고...") String content,
      @Schema(description = "리뷰 키워드 문구 목록", example = "[\"요청사항을 잘 들어주셨어요\"]") List<String> keywords,
      @Schema(description = "케이크 완성 이미지 (옵션 반영된 이미지)", example = "https://s3...")
          String cakeImageUrl,
      @Schema(description = "케이크 주문 옵션 상세") CakeOptionDTO options) {}

  @Builder
  public record CakeOptionDTO(
      @Schema(description = "디자인 이름 (1호 원형 등)", example = "1호 원형") String designName,
      @Schema(description = "맛 (시트 + 생크림)", example = "초코 시트 + 생크림") String taste,
      @Schema(description = "데코 (토핑)", example = "오레오") String deco,
      @Schema(description = "추가 요청 사항", example = "크리스마스 토퍼") String additionalRequest,
      @Schema(description = "선택된 컬러 정보 (아이싱, 시트, 크림)") CakeColorsDTO colors) {}

  @Builder
  public record CakeColorsDTO(
      @Schema(description = "아이싱 컬러 코드", example = "#F4E0C9") String icingColor,
      @Schema(description = "시트 컬러 코드", example = "#8B4513") String sheetColor,
      @Schema(description = "크림 컬러 코드", example = "#D3EBFA") String creamColor) {}
}
