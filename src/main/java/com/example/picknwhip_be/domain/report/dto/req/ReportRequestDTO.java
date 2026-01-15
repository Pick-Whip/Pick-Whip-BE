package com.example.picknwhip_be.domain.report.dto.req;

import com.example.picknwhip_be.domain.report.entity.ReportReasonType;
import com.example.picknwhip_be.domain.report.entity.ReportTargetType;
import lombok.Getter;

public class ReportRequestDTO {

  @Getter
  public static class CreateReportDTO {
    private Long shopId;
    private ReportTargetType targetType;
    private ReportReasonType reasonType;
    private String content;
  }
}
