package com.example.picknwhip_be.domain.report.service;

import com.example.picknwhip_be.domain.report.dto.req.ReportRequestDTO;
import com.example.picknwhip_be.domain.report.entity.Report;

public interface ReportCommandService {
  Report createReport(Long userId, ReportRequestDTO.CreateReportDTO request);
}
