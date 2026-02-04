package com.example.picknwhip_be.domain.order.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderReqDTO {

  @Getter
  @NoArgsConstructor
  public static class CreateOrderDTO {
    @NotNull
    @Schema(description = "임시저장된 주문서 ID", example = "1")
    private Long draftId;

    @NotBlank
    @Schema(description = "주문자 이름 (픽업자)", example = "김철수")
    private String customerName;

    @NotBlank
    @Schema(description = "주문자 연락처", example = "010-1234-5678")
    private String customerPhone;

    @Schema(description = "주문 관련 추가 요청사항 (예: 초는 3개 주세요)", example = "포크는 빼주세요")
    private String additionalRequest;
  }
}
