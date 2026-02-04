package com.example.picknwhip_be.domain.report.controller;

import com.example.picknwhip_be.domain.report.dto.req.ReportReqDTO;
import com.example.picknwhip_be.domain.report.service.command.ReportCommandService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Report", description = "신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportRestController {

  private final ReportCommandService reportCommandService;

  @Operation(summary = "신고하기 API", description = "로그인된 사용자가 신고를 접수합니다.")
  @PostMapping("")
  public ApiResponse<String> createReport(
      @ExtractPayload Long userId, @Valid @RequestBody ReportReqDTO.CreateReportDTO request) {

    reportCommandService.createReport(userId, request);
    return ApiResponse.of(GeneralSuccessCode.CREATED, "신고가 접수되었습니다.");
  }
}
