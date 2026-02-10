package com.example.picknwhip_be.domain.home.scheduler;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.home.entity.PopularCakeRanking;
import com.example.picknwhip_be.domain.home.repository.PopularCakeRankingRepository;
import com.example.picknwhip_be.domain.payment.repository.PaymentRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PopularCakeRankingScheduler {

  private final PaymentRepository paymentRepository;
  private final PopularCakeRankingRepository rankingRepository;
  private final EntityManager em;

  private static final ZoneId KST = ZoneId.of("Asia/Seoul");

  // 매일 00:00:00 (자정) KST 실행
  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  @Transactional
  @CacheEvict(value = "popularCakes", allEntries = true)
  public void refreshTop5() {
    ZonedDateTime nowKst = ZonedDateTime.now(KST);

    // 자정 실행 시점 기준 "최근 14일"
    LocalDateTime endAt = nowKst.toLocalDateTime();
    LocalDateTime startAt = nowKst.minusDays(14).toLocalDateTime();

    List<PaymentRepository.PopularCakeAgg> top5 =
        paymentRepository.findPopularCakesTop5(startAt, endAt);

    // 최신 Top5로 완전 교체
    rankingRepository.deleteAllInBatch();

    if (top5.isEmpty()) {
      return;
    }

    List<PopularCakeRanking> toSave = new ArrayList<>();
    int rankNum = 1;

    for (PaymentRepository.PopularCakeAgg row : top5) {
      DesignGallery designRef = em.getReference(DesignGallery.class, row.getDesignId());
      Shop shopRef = em.getReference(Shop.class, row.getShopId());

      toSave.add(
          PopularCakeRanking.builder()
              .ranking(rankNum++)
              .design(designRef)
              .shop(shopRef)
              .orderCount(row.getOrderCount())
              .windowStart(startAt)
              .windowEnd(endAt)
              .calculatedAt(endAt)
              .build());
    }

    rankingRepository.saveAll(toSave);
  }
}
