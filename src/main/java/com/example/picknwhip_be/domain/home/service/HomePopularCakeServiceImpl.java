package com.example.picknwhip_be.domain.home.service;

import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.favorite.service.DesignMyPickCheckerService;
import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResDTO;
import com.example.picknwhip_be.domain.home.repository.PopularCakeRankingRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomePopularCakeServiceImpl implements HomePopularCakeService {

  private final PopularCakeRankingRepository rankingRepository;
  private final DesignMyPickCheckerService designMyPickChecker;
  private final S3Service s3Service;

  @Override
  @Transactional(readOnly = true)
  public List<PopularCakeResDTO> getPopularCakesTop5(Long userId) {

    List<PopularCakeResDTO> cachedRankings = rankingRepository.findTop5Rankings();

    if (cachedRankings == null || cachedRankings.isEmpty()) {
      log.warn("인기 케이크 랭킹 데이터가 비어있습니다. 스케줄러가 아직 실행되지 않았을 수 있습니다.");
      return Collections.emptyList();
    }

    List<PopularCakeResDTO> presignedConverted =
        cachedRankings.stream()
            .map(
                dto -> {
                  String raw = dto.getCakeImageUrl();
                  if (raw == null || raw.isBlank() || isHttpUrl(raw)) {
                    return dto;
                  }
                  String presigned = s3Service.createPresignedDownloadUrl(raw);
                  return dto.toBuilder().cakeImageUrl(presigned).build();
                })
            .toList();

    List<Long> designIds = cachedRankings.stream().map(PopularCakeResDTO::getDesignId).toList();
    Set<Long> pickedDesignIds = new HashSet<>();

    if (userId != null && !designIds.isEmpty()) {
      pickedDesignIds.addAll(designMyPickChecker.findPickedDesignIds(userId, designIds));
    }

    if (pickedDesignIds.isEmpty()) {
      return List.copyOf(cachedRankings);
    }

    return cachedRankings.stream()
        .map(dto -> dto.toBuilder().isMyPick(pickedDesignIds.contains(dto.getDesignId())).build())
        .collect(Collectors.toList());
  }

  private boolean isHttpUrl(String value) {
    return value.startsWith("http://") || value.startsWith("https://");
  }
}
