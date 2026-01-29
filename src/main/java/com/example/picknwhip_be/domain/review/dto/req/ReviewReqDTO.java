package com.example.picknwhip_be.domain.review.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.List;

public class ReviewReqDTO {
  // 리뷰 작성 DTO
  public record WriteDTO(
      @Schema(description = "별점(1~5의 정수)", example = "5") @NotNull @Min(1) @Max(5) Integer rating,
      @Schema(description = "리뷰 내용(10~500자)", example = "너무 예쁘고 맛있었어요!")
          @NotBlank
          @Size(min = 10, max = 500)
          String content,
      @Schema(description = "선택된 키워드 코드(1~5개)", example = "[\"DELICIOUS\", \"CLEAN_STORE\"]")
          @NotEmpty
          @Size(min = 1, max = 5)
          List<String> keywords,
      @Schema(description = "S3에 업로드된 이미지 key 목록(최대 5장)", example = "[\"1.jpg\",  \"2.jpg\"]")
          @Size(max = 5)
          List<String> imageKeys,
      @Schema(description = "옵션 공개 동의 여부", example = "true") Boolean agreement) {}
}
