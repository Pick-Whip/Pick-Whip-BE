package com.example.picknwhip_be.domain.report.dto.req;

import com.example.picknwhip_be.domain.report.entity.ReportReasonType;
import com.example.picknwhip_be.domain.report.entity.ReportTargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportRequestDTO {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateReportDTO {
    private Long targetId;
    private ReportTargetType targetType;
    private ReportReasonType reasonType;
    private String content;
  }
}
