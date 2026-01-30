package com.example.picknwhip_be.domain.home.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.favorite.service.DesignMyPickChecker;
import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResponseDto;
import com.example.picknwhip_be.domain.home.entity.PopularCakeRanking;
import com.example.picknwhip_be.domain.home.exception.HomeException;
import com.example.picknwhip_be.domain.home.exception.code.HomeErrorCode;
import com.example.picknwhip_be.domain.home.repository.PopularCakeRankingRepository;
import com.example.picknwhip_be.domain.home.service.serviceImpl.HomePopularCakeServiceImpl;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HomePopularCakeServiceTest {

  @InjectMocks private HomePopularCakeServiceImpl homePopularCakeService;

  @Mock private PopularCakeRankingRepository rankingRepository;

  @Mock private DesignMyPickChecker designMyPickChecker;

  @Test
  @DisplayName("인기 케이크 Top5 조회 성공 - 로그인 유저 (마이픽 포함)")
  void getPopularCakesTop5_Success_User() {
    Long userId = 1L;
    Long pickedDesignId = 100L;
    Long unpickedDesignId = 200L;

    DesignGallery design1 = mock(DesignGallery.class);
    given(design1.getId()).willReturn(pickedDesignId);
    given(design1.getDesignName()).willReturn("초코 케이크");
    given(design1.getImageUrl()).willReturn("img1.jpg");

    Shop shop1 = mock(Shop.class);
    given(shop1.getId()).willReturn(10L);
    given(shop1.getShopName()).willReturn("맛나 베이커리");
    given(shop1.getAverageRating()).willReturn(4.5);
    given(shop1.getMinPrice()).willReturn(30000);

    PopularCakeRanking ranking1 =
        PopularCakeRanking.builder().ranking(1).design(design1).shop(shop1).orderCount(50L).build();

    // 2위 케이크 (마이픽 안함)
    DesignGallery design2 = mock(DesignGallery.class);
    given(design2.getId()).willReturn(unpickedDesignId);

    Shop shop2 = mock(Shop.class);

    PopularCakeRanking ranking2 =
        PopularCakeRanking.builder().ranking(2).design(design2).shop(shop2).orderCount(30L).build();

    given(rankingRepository.findTop5WithDesignAndShop()).willReturn(List.of(ranking1, ranking2));
    given(designMyPickChecker.findPickedDesignIds(eq(userId), anyList()))
        .willReturn(Set.of(pickedDesignId));

    List<PopularCakeResponseDto> result = homePopularCakeService.getPopularCakesTop5(userId);

    assertThat(result).hasSize(2);

    // 1위 검증 (마이픽 O)
    assertThat(result.get(0).getRank()).isEqualTo(1);
    assertThat(result.get(0).getCakeName()).isEqualTo("초코 케이크");
    assertThat(result.get(0).isMyPick()).isTrue();

    // 2위 검증 (마이픽 X)
    assertThat(result.get(1).getDesignId()).isEqualTo(unpickedDesignId);
    assertThat(result.get(1).isMyPick()).isFalse();
  }

  @Test
  @DisplayName("인기 케이크 Top5 조회 성공 - 비로그인 유저 (마이픽 체크 안함)")
  void getPopularCakesTop5_Success_Guest() {
    // given
    Long userId = null;

    DesignGallery design = mock(DesignGallery.class);
    given(design.getId()).willReturn(100L);
    Shop shop = mock(Shop.class);

    PopularCakeRanking ranking =
        PopularCakeRanking.builder().ranking(1).design(design).shop(shop).orderCount(10L).build();

    given(rankingRepository.findTop5WithDesignAndShop()).willReturn(List.of(ranking));

    List<PopularCakeResponseDto> result = homePopularCakeService.getPopularCakesTop5(userId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).isMyPick()).isFalse();

    verify(designMyPickChecker, never()).findPickedDesignIds(any(), anyList());
  }

  @Test
  @DisplayName("집계된 인기 케이크가 없을 경우 예외 발생")
  void getPopularCakesTop5_Empty_ThrowsException() {
    given(rankingRepository.findTop5WithDesignAndShop()).willReturn(List.of());

    assertThatThrownBy(() -> homePopularCakeService.getPopularCakesTop5(1L))
        .isInstanceOf(HomeException.class)
        .hasFieldOrPropertyWithValue("errorCode", HomeErrorCode.POPULAR_CAKE_RANKING_NOT_READY);
  }
}
