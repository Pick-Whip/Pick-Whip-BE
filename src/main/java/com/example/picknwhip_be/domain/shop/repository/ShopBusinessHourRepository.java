package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.ShopBusinessHour;
import com.example.picknwhip_be.domain.shop.entity.enums.ScheduleType;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopBusinessHourRepository extends JpaRepository<ShopBusinessHour, Long> {

  // 특정 날짜 예외 스케줄 (휴무일 등) 조회
  Optional<ShopBusinessHour> findByShopIdAndDateAndScheduleType(
      Long shopId, LocalDate date, ScheduleType scheduleType);

  // 요일별 정기 스케줄 조회
  Optional<ShopBusinessHour> findByShopIdAndDayOfWeekAndScheduleType(
      Long shopId, Integer dayOfWeek, ScheduleType scheduleType);
}
