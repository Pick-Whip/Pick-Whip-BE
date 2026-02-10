package com.example.picknwhip_be.domain.home.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.home.entity.PopularDesignRanking;
import com.example.picknwhip_be.domain.home.repository.PopularDesignRankingRepository;
import com.example.picknwhip_be.domain.payment.repository.PaymentRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PopularDesignRankingSchedulerTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private PopularDesignRankingRepository rankingRepository;
    @Mock private EntityManager em;

    @InjectMocks private PopularDesignRankingScheduler scheduler;

    @Test
    @DisplayName("인기 디자인 랭킹 갱신 로직 검증")
    void refreshDesignRankingTest() {
        // given
        // 1. Mock Aggregate Data (DB에서 가져올 집계 결과 모킹)
        PaymentRepository.PopularDesignAgg agg1 = mock(PaymentRepository.PopularDesignAgg.class);
        when(agg1.getDesignId()).thenReturn(1L);
        when(agg1.getShopId()).thenReturn(10L);
        when(agg1.getOrderCount()).thenReturn(50L);

        PaymentRepository.PopularDesignAgg agg2 = mock(PaymentRepository.PopularDesignAgg.class);
        when(agg2.getDesignId()).thenReturn(2L);
        when(agg2.getShopId()).thenReturn(10L);
        when(agg2.getOrderCount()).thenReturn(30L);

        List<PaymentRepository.PopularDesignAgg> mockAggregates = List.of(agg1, agg2);

        // 2. Stubbing behavior
        when(paymentRepository.findTop4DesignByOrders(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockAggregates);

        // EntityManager Stubbing (프록시 객체 반환)
        when(em.getReference(DesignGallery.class, 1L)).thenReturn(mock(DesignGallery.class));
        when(em.getReference(DesignGallery.class, 2L)).thenReturn(mock(DesignGallery.class));
        when(em.getReference(Shop.class, 10L)).thenReturn(mock(Shop.class));

        // when
        scheduler.refreshDesignRanking();

        // then
        // 1. 기존 데이터 삭제가 호출되었는가?
        verify(rankingRepository, times(1)).deleteAllInBatch();

        // 2. 집계 쿼리가 호출되었는가?
        verify(paymentRepository, times(1))
                .findTop4DesignByOrders(any(LocalDateTime.class), any(LocalDateTime.class));

        // 3. 새로운 데이터 저장이 호출되었는가?
        verify(rankingRepository, times(1)).saveAll(anyList());
    }
}