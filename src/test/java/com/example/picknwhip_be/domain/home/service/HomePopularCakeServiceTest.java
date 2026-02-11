package com.example.picknwhip_be.domain.home.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.picknwhip_be.domain.favorite.service.DesignMyPickCheckerService;
import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResDTO;
import com.example.picknwhip_be.domain.home.repository.PopularCakeRankingRepository;
import java.util.Collections;
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

  @Mock private DesignMyPickCheckerService designMyPickChecker;

  @Test
  @DisplayName("인기 케이크 Top5 조회 성공 - 로그인 유저 (마이픽 포함)")
  void getPopularCakesTop5_Success_User() {
    // given
    Long userId = 1L;
    Long pickedDesignId = 100L;
    Long unpickedDesignId = 200L;

    // [Pick&Whip 수정] Entity 대신 DTO를 Mocking 합니다.
    // 1위: 찜한 케이크
    PopularCakeResDTO dto1 =
        PopularCakeResDTO.builder()
            .rank(1)
            .designId(pickedDesignId)
            .cakeName("초코 케이크")
            .shopName("맛나 베이커리")
            .isMyPick(false) // DB에서 가져올 땐 false
            .build();

    // 2위: 찜 안 한 케이크
    PopularCakeResDTO dto2 =
        PopularCakeResDTO.builder()
            .rank(2)
            .designId(unpickedDesignId)
            .cakeName("딸기 케이크")
            .isMyPick(false) // DB에서 가져올 땐 false
            .build();

    // Repository는 DTO 리스트를 반환한다고 가정
    given(rankingRepository.findTop5Rankings()).willReturn(List.of(dto1, dto2));

    // 찜 목록 확인 로직 Mocking
    given(designMyPickChecker.findPickedDesignIds(eq(userId), anyList()))
        .willReturn(Set.of(pickedDesignId));

    // when
    List<PopularCakeResDTO> result = homePopularCakeService.getPopularCakesTop5(userId);

    // then
    assertThat(result).hasSize(2);

    // 1위 검증 (Service 로직을 거치며 isMyPick이 true로 변해야 함)
    assertThat(result.get(0).getRank()).isEqualTo(1);
    assertThat(result.get(0).getCakeName()).isEqualTo("초코 케이크");
    assertThat(result.get(0).isMyPick()).isTrue(); // True 확인!

    // 2위 검증 (isMyPick False 유지)
    assertThat(result.get(1).getDesignId()).isEqualTo(unpickedDesignId);
    assertThat(result.get(1).isMyPick()).isFalse();
  }

  @Test
  @DisplayName("인기 케이크 Top5 조회 성공 - 비로그인 유저 (마이픽 체크 안함)")
  void getPopularCakesTop5_Success_Guest() {
    // given
    Long userId = null;

    PopularCakeResDTO dto =
        PopularCakeResDTO.builder().rank(1).designId(100L).isMyPick(false).build();

    given(rankingRepository.findTop5Rankings()).willReturn(List.of(dto));

    // when
    List<PopularCakeResDTO> result = homePopularCakeService.getPopularCakesTop5(userId);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).isMyPick()).isFalse();

    // 비로그인이면 찜 확인 로직 호출 X
    verify(designMyPickChecker, never()).findPickedDesignIds(any(), anyList());
  }

  @Test
  @DisplayName("집계된 인기 케이크가 없을 경우 - 예외 대신 빈 리스트 반환")
  void getPopularCakesTop5_Empty_ReturnsEmptyList() {
    // given
    // [Pick&Whip 수정] 데이터가 없으면 null 또는 빈 리스트를 반환
    given(rankingRepository.findTop5Rankings()).willReturn(Collections.emptyList());

    // when
    List<PopularCakeResDTO> result = homePopularCakeService.getPopularCakesTop5(1L);

    // then
    // [Pick&Whip 수정] 예외가 터지는지 확인하는 게 아니라, 빈 리스트인지 확인
    assertThat(result).isEmpty();
  }
}
