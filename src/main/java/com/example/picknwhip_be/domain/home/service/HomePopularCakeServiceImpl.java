package com.example.picknwhip_be.domain.home.service;

import com.example.picknwhip_be.domain.favorite.service.DesignMyPickChecker;
import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResDTO;
import com.example.picknwhip_be.domain.home.entity.PopularCakeRanking;
import com.example.picknwhip_be.domain.home.exception.HomeException;
import com.example.picknwhip_be.domain.home.exception.code.HomeErrorCode;
import com.example.picknwhip_be.domain.home.repository.PopularCakeRankingRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomePopularCakeServiceImpl implements HomePopularCakeService {

  private final PopularCakeRankingRepository rankingRepository;
  private final DesignMyPickChecker designMyPickChecker;

  @Override
  @Transactional(readOnly = true)
  public List<PopularCakeResDTO> getPopularCakesTop5(Long userId) {

    List<PopularCakeRanking> rankings = rankingRepository.findTop5WithDesignAndShop();

    if (rankings.isEmpty()) {
      throw new HomeException(HomeErrorCode.POPULAR_CAKE_RANKING_NOT_READY);
    }

    List<Long> designIds = rankings.stream().map(r -> r.getDesign().getId()).toList();

    Set<Long> pickedDesignIds = new HashSet<>();
    if (userId != null && !designIds.isEmpty()) {
      pickedDesignIds.addAll(designMyPickChecker.findPickedDesignIds(userId, designIds));
    }

    return rankings.stream()
        .map(
            r ->
                PopularCakeResDTO.builder()
                    .rank(r.getRanking())
                    .designId(r.getDesign().getId())
                    .cakeName(r.getDesign().getDesignName())
                    .cakeImageUrl(r.getDesign().getImageUrl())
                    .shopId(r.getShop().getId())
                    .shopName(r.getShop().getShopName())
                    .averageRating(r.getShop().getAverageRating())
                    .minPrice(r.getShop().getMinPrice())
                    .isMyPick(pickedDesignIds.contains(r.getDesign().getId()))
                    .orderCount(r.getOrderCount())
                    .build())
        .toList();
  }
}
