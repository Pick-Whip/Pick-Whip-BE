package com.example.picknwhip_be.domain.report.service.command;

import com.example.picknwhip_be.domain.notification.enums.NotificationKind;
import com.example.picknwhip_be.domain.notification.event.CreateNotificationEvent;
import com.example.picknwhip_be.domain.report.converter.ReportConverter;
import com.example.picknwhip_be.domain.report.dto.req.ReportReqDTO;
import com.example.picknwhip_be.domain.report.entity.Report;
import com.example.picknwhip_be.domain.report.exception.ReportException;
import com.example.picknwhip_be.domain.report.exception.code.ReportErrorCode;
import com.example.picknwhip_be.domain.report.repository.ReportRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportCommandServiceImpl implements ReportCommandService {

  private final ReportRepository reportRepository;
  private final UserQueryService userQueryService;
  private final ApplicationEventPublisher publisher;

  @Override
  @Transactional
  public Report createReport(Long userId, ReportReqDTO.CreateReportDTO request) {
    User reporter = userQueryService.getUser(userId);

    // 중복 신고 방지: 동일 신고자 + 동일 대상 조합으로 신고가 존재하면 예외
    if (reportRepository.existsByReporterAndTargetIdAndTargetType(
        reporter, request.getTargetId(), request.getTargetType())) {
      throw new ReportException(ReportErrorCode.DUPLICATE_REPORT);
    }

    /*
     * [TODO: 검색 기능 연동]
     * 1. 현재 신고 대상이 '케이크샵(SHOP)'인 경우, 프론트엔드에서 가게명 검색 기능을 통해
     * 리스트에서 선택된 정확한 shopId가 넘어오는 구조입니다.
     * 2. 현재 검색 기능이 미구현 상태이므로, 전달받은 shopId의 유효성 검증 로직은
     * 검색 API 파트 개발 완료 후 연동할 예정
     */

    Report newReport = ReportConverter.toReport(request, reporter);
    Report saved = reportRepository.save(newReport);
    Long reportId = saved.getReportId();
    publisher.publishEvent(
        new CreateNotificationEvent(userId, NotificationKind.REPORT_RECEIVED, reportId, null));

    return saved;
  }
}
