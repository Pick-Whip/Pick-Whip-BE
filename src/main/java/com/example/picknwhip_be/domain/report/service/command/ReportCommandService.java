package com.example.picknwhip_be.domain.report.service.command;

import com.example.picknwhip_be.domain.report.dto.req.ReportReqDTO;
import com.example.picknwhip_be.domain.report.entity.Report;

public interface ReportCommandService {
  Report createReport(Long userId, ReportReqDTO.CreateReportDTO request);
}
