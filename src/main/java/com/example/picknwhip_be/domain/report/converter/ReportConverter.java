package com.example.picknwhip_be.domain.report.converter;

import com.example.picknwhip_be.domain.report.dto.req.ReportRequestDTO;
import com.example.picknwhip_be.domain.report.entity.Report;
import com.example.picknwhip_be.domain.user.entity.User;

public class ReportConverter {
  public static Report toReport(ReportRequestDTO.CreateReportDTO request, User reporter) {
    return Report.builder()
        .reporter(reporter)
        .targetId(request.getTargetId())
        .targetType(request.getTargetType())
        .reasonType(request.getReasonType())
        .content(request.getContent())
        .build();
  }
}
