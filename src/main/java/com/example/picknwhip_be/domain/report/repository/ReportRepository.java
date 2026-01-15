package com.example.picknwhip_be.domain.report.repository;

import com.example.picknwhip_be.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {}
