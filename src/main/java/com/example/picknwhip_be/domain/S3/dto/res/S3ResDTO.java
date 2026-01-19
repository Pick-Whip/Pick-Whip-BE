package com.example.picknwhip_be.domain.S3.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

public class S3ResDTO {
  public record PresignResponseDTO(
      @Schema(description = "저장 경로", example = "review/.../1.jpg") String keyName,
      @Schema(
              description = "presigned url",
              example = "https://picknwhip.s3.ap-northeast-2.amazonaws.com/review/.../1.jpg?...")
          String url) {}
}
