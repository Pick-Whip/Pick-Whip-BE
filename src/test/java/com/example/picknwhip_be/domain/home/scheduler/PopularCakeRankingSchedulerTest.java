package com.example.picknwhip_be.domain.home.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.home.entity.PopularCakeRanking;
import com.example.picknwhip_be.domain.home.repository.PopularCakeRankingRepository;
import com.example.picknwhip_be.domain.payment.repository.PaymentRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PopularCakeRankingSchedulerTest {

  @InjectMocks private PopularCakeRankingScheduler scheduler;

  @Mock private PaymentRepository paymentRepository;

  @Mock private PopularCakeRankingRepository rankingRepository;

  @Mock private EntityManager em;

  @Test
  @DisplayName("Top5 갱신 스케줄러 실행 시 기존 데이터 삭제 후 새 데이터 저장")
  void refreshTop5_Success() {
    PaymentRepository.PopularCakeAgg agg1 = mock(PaymentRepository.PopularCakeAgg.class);
    given(agg1.getDesignId()).willReturn(10L);
    given(agg1.getShopId()).willReturn(20L);
    given(agg1.getOrderCount()).willReturn(100L);

    PaymentRepository.PopularCakeAgg agg2 = mock(PaymentRepository.PopularCakeAgg.class);
    given(agg2.getDesignId()).willReturn(11L);
    given(agg2.getShopId()).willReturn(21L);
    given(agg2.getOrderCount()).willReturn(80L);

    given(
            paymentRepository.findPopularCakesTop5(
                any(LocalDateTime.class), any(LocalDateTime.class)))
        .willReturn(List.of(agg1, agg2));

    DesignGallery mockDesign = mock(DesignGallery.class);
    Shop mockShop = mock(Shop.class);
    given(em.getReference(eq(DesignGallery.class), any())).willReturn(mockDesign);
    given(em.getReference(eq(Shop.class), any())).willReturn(mockShop);

    scheduler.refreshTop5();
    verify(rankingRepository).deleteAllInBatch();

    ArgumentCaptor<List<PopularCakeRanking>> captor = ArgumentCaptor.forClass(List.class);
    verify(rankingRepository).saveAll(captor.capture());

    List<PopularCakeRanking> savedList = captor.getValue();
    assertThat(savedList).hasSize(2);

    assertThat(savedList.get(0).getRank()).isEqualTo(1);
    assertThat(savedList.get(0).getOrderCount()).isEqualTo(100L);

    assertThat(savedList.get(1).getRank()).isEqualTo(2);
    assertThat(savedList.get(1).getOrderCount()).isEqualTo(80L);
  }
}
