package com.example.picknwhip_be.domain.report.repository;

import com.example.picknwhip_be.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
  boolean existsByReporterAndTargetIdAndTargetType(
      com.example.picknwhip_be.domain.user.entity.User reporter,
      Long targetId,
      com.example.picknwhip_be.domain.report.entity.ReportTargetType targetType);
}
