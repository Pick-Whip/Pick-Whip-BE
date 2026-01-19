package com.example.picknwhip_be.domain.S3.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class S3ReqDTO {
  // 단일 요청 DTO
  public record SingleDTO(
      @Schema(description = "이미지 파일명", example = "1.jpg") @NotBlank String fileName) {}

  // 다중 요청 DTO
  public record BatchDTO(
      @Schema(description = "이미지 파일명 리스트", example = "[\"1.jpg\", \"2.jpg\"]")
          @NotEmpty
          @Size(max = 5)
          List<@NotBlank String> fileNames) {}
}
