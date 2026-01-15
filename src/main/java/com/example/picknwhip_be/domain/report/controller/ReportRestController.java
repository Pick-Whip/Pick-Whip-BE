package com.example.picknwhip_be.domain.report.controller;

import com.example.picknwhip_be.domain.report.dto.req.ReportRequestDTO;
import com.example.picknwhip_be.domain.report.service.ReportCommandService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Report", description = "신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportRestController {

  private final ReportCommandService reportCommandService;

  @Operation(summary = "신고하기 API", description = "로그인된 사용자가 신고를 접수합니다.")
  @PostMapping("")
  public ApiResponse<String> createReport(@RequestBody ReportRequestDTO.CreateReportDTO request) {
    // TODO: SecurityContext 연동 필요
    Long userId = 1L;
    reportCommandService.createReport(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, "신고가 접수되었습니다.");
  }
}
