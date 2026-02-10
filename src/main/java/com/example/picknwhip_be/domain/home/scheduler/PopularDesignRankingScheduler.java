package com.example.picknwhip_be.domain.home.scheduler;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.home.entity.PopularDesignRanking;
import com.example.picknwhip_be.domain.home.repository.PopularDesignRankingRepository;
import com.example.picknwhip_be.domain.payment.repository.PaymentRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PopularDesignRankingScheduler {

  private final PaymentRepository paymentRepository;
  private final PopularDesignRankingRepository rankingRepository;
  private final EntityManager em;

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정 실행
  @Transactional
  public void refreshDesignRanking() {
    ZonedDateTime nowKst = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    LocalDateTime endAt = nowKst.toLocalDateTime();
    LocalDateTime startAt = nowKst.minusDays(14).toLocalDateTime();

    List<PaymentRepository.PopularDesignAgg> top4 =
        paymentRepository.findTop4DesignByOrders(startAt, endAt);

    rankingRepository.deleteAllInBatch();

    List<PopularDesignRanking> toSave = new ArrayList<>();
    int rankNum = 1;

    for (PaymentRepository.PopularDesignAgg agg : top4) {
      toSave.add(
          PopularDesignRanking.builder()
              .ranking(rankNum++)
              .design(em.getReference(DesignGallery.class, agg.getDesignId()))
              .shop(em.getReference(Shop.class, agg.getShopId()))
              .orderCount(agg.getOrderCount())
              .calculatedAt(endAt)
              .build());
    }
    rankingRepository.saveAll(toSave);
  }
}
