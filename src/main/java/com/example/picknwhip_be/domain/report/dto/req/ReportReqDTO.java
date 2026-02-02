package com.example.picknwhip_be.domain.report.dto.req;

import com.example.picknwhip_be.domain.report.entity.ReportReasonType;
import com.example.picknwhip_be.domain.report.entity.ReportTargetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportReqDTO {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateReportDTO {
    @NotNull private Long targetId;
    @NotNull private ReportTargetType targetType;
    @NotNull private ReportReasonType reasonType;

    @NotNull
    @Size(max = 100)
    private String content;
  }
}
