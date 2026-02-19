package com.example.picknwhip_be.domain.shop.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.example.picknwhip_be.domain.order.exception.OrderException;
import com.example.picknwhip_be.domain.order.repository.OrderRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopBusinessHourRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PickupTimeValidatorTest {

  private ShopBusinessHourRepository businessHourRepository;
  private OrderRepository orderRepository;
  private TimeZone originalTimeZone;

  @BeforeEach
  void setUp() {
    // 1. 현재 테스트가 실행되는 환경(OS)의 시간대를 저장해둡니다.
    originalTimeZone = TimeZone.getDefault();

    // 2. 핵심: 서버 환경을 강제로 UTC(영국 시간)로 변경합니다. (AWS EC2 기본 환경 모방)
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    businessHourRepository = mock(ShopBusinessHourRepository.class);
    orderRepository = mock(OrderRepository.class);
  }

  @AfterEach
  void tearDown() {
    // 테스트가 끝나면 다른 테스트에 영향을 주지 않도록 원래 시간대로 롤백합니다.
    TimeZone.setDefault(originalTimeZone);
  }

  @Test
  @DisplayName("서버 시간이 UTC여도, KST Clock이 주입되면 한국 시간을 기준으로 과거/미래를 정확히 판단한다")
  void validate_ShouldUseKST_WhenServerIsUTC() {
    // given
    // 현재 전 세계 절대 시간: 2026-02-20 05:00:00 (UTC 기준)
    Instant currentInstant = Instant.parse("2026-02-20T05:00:00Z");

    // TimeConfig에 등록하신 kstClock 빈(Bean)과 완전히 동일한 상태의 시계 생성
    Clock kstClock = Clock.fixed(currentInstant, ZoneId.of("Asia/Seoul"));

    // Validator에 KST 시계 주입
    PickupTimeValidator validator =
        new PickupTimeValidator(businessHourRepository, orderRepository, kstClock);

    Shop shop = Shop.builder().id(1L).slotIntervalMinutes(30).build();

    // 픽업 요청 시간: 2026년 2월 20일 오전 10시 00분
    LocalDateTime pickupTime = LocalDateTime.of(2026, 2, 20, 10, 0);

    /* * [상황 분석]
     * 1. 시스템(서버) 시간 기준 현재는 05:00 -> 픽업 요청(10:00)은 '미래'입니다.
     * 2. 한국 시간(KST) 기준 현재는 14:00 (05:00 + 9시간) -> 픽업 요청(10:00)은 '과거'입니다.
     * * 만약 코드가 KST로 동작하지 않고 서버 시간을 따라간다면, 에러가 발생하지 않을 것입니다.
     * 반대로 코드가 완벽히 KST로 동작한다면, 과거 시간 예약으로 판단하여 에러를 던져야 합니다!
     */

    // when & then
    // 한국 시간 기준으로 10:00은 이미 지나간(과거) 시간이므로 INVALID_PICKUP_TIME 예외가 터져야 성공!
    assertThatThrownBy(() -> validator.validate(shop, pickupTime))
        .isInstanceOf(OrderException.class);
    // 추가로 에러 코드 검증: .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.INVALID_PICKUP_TIME)
    // 등으로 상세 검증 가능
  }
}
