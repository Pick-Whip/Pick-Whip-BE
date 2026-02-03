package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.order.entity.DailyShopOrderCounter;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface DailyShopOrderCounterRepository
    extends JpaRepository<DailyShopOrderCounter, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  @Query("SELECT c FROM DailyShopOrderCounter c WHERE c.shopId = :shopId AND c.date = :date")
  Optional<DailyShopOrderCounter> findByShopIdAndDateWithLock(
      @Param("shopId") Long shopId, @Param("date") LocalDate date);
}
