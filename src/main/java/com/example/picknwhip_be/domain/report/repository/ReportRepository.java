package com.example.picknwhip_be.domain.report.repository;

import com.example.picknwhip_be.domain.report.entity.Report;
import com.example.picknwhip_be.domain.report.entity.ReportTargetType;
import com.example.picknwhip_be.domain.review.service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
  boolean existsByReporterAndTargetIdAndTargetType(
      User reporter, Long targetId, ReportTargetType targetType);
}
