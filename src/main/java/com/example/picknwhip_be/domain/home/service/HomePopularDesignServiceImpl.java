package com.example.picknwhip_be.domain.home.service;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.design.entity.mapping.DesignOption;
import com.example.picknwhip_be.domain.design.enums.Style;
import com.example.picknwhip_be.domain.favorite.service.DesignMyPickCheckerService;
import com.example.picknwhip_be.domain.home.dto.res.PopularDesignResDTO;
import com.example.picknwhip_be.domain.home.entity.PopularDesignRanking;
import com.example.picknwhip_be.domain.home.exception.HomeException;
import com.example.picknwhip_be.domain.home.exception.code.HomeErrorCode;
import com.example.picknwhip_be.domain.home.repository.PopularDesignRankingRepository;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomePopularDesignServiceImpl implements HomePopularDesignService {

  private final PopularDesignRankingRepository rankingRepository;
  private final DesignMyPickCheckerService designMyPickChecker;

  @Override
  @Transactional(readOnly = true)
  public List<PopularDesignResDTO> getPopularDesignsTop4(Long userId) {
    List<PopularDesignRanking> rankings = rankingRepository.findTop4WithDetails();

    if (rankings.isEmpty()) {
      throw new HomeException(HomeErrorCode.POPULAR_DESIGN_RANKING_NOT_READY);
    }
    Set<Long> pickedDesignIds = Collections.emptySet();
    if (userId != null) {
      List<Long> designIds = rankings.stream().map(r -> r.getDesign().getId()).toList();
      pickedDesignIds = designMyPickChecker.findPickedDesignIds(userId, designIds);
    }

    Set<Long> finalPickedDesignIds = pickedDesignIds;
    return rankings.stream()
        .map(r -> convertToDto(r, finalPickedDesignIds.contains(r.getDesign().getId())))
        .toList();
  }

  private PopularDesignResDTO convertToDto(PopularDesignRanking ranking, boolean isPicked) {
    DesignGallery d = ranking.getDesign();

    String sizeName = "-";
    if (d.getShopCakeSize() != null) {
      sizeName = d.getShopCakeSize().getSizeName();
    }

    String shapeName = findShapeFromKeywords(d.getKeywords());
    if (shapeName == null) {
      shapeName = findOptionName(d.getOptions(), OptionCategory.SHAPE);
    }
    String designSpec =
        sizeName + (shapeName.equals("기타") || shapeName.equals("선택 안함") ? "" : " " + shapeName);

    String sheet = findOptionName(d.getOptions(), OptionCategory.SHEET);
    String cream = findOptionName(d.getOptions(), OptionCategory.CREAM);

    String flavorSpec = "기타 맛";
    if (!sheet.equals("선택 안함") || !cream.equals("선택 안함")) {
      flavorSpec =
          (sheet.equals("선택 안함") ? "" : sheet)
              + (!sheet.equals("선택 안함") && !cream.equals("선택 안함") ? " + " : "")
              + (cream.equals("선택 안함") ? "" : cream);
    }

    String align = "";
    if (d.getLetteringAlignment() != null) {
      align = d.getLetteringAlignment().getDescription() + " 정렬";
    }

    String line = "";
    if (d.getLetteringLineCount() != null) {
      line = d.getLetteringLineCount().getDescription().replace("줄", "번");
    }

    String letteringOption = "-";
    if (!align.isEmpty() || !line.isEmpty()) {
      if (!align.isEmpty() && !line.isEmpty()) {
        letteringOption = align + " + " + line;
      } else {
        letteringOption = align + line;
      }
    }

    return PopularDesignResDTO.builder()
        .ranking(ranking.getRanking())
        .designId(d.getId())
        .shopName(ranking.getShop().getShopName())
        .cakeName(d.getDesignName())
        .imageUrl(d.getImageUrl())
        .designSpec(designSpec.trim())
        .flavorSpec(flavorSpec)
        .letteringPhrase(d.getLetteringText())
        .letteringOption(letteringOption)
        .isMyPick(isPicked)
        .build();
  }

  private String findShapeFromKeywords(Set<Style> keywords) {
    if (keywords == null) return null;
    return keywords.stream()
        .filter(k -> k == Style.ROUND || k == Style.HEART || k == Style.SQUARE)
        .map(Style::getLabel)
        .findFirst()
        .orElse(null);
  }

  private String findOptionName(List<DesignOption> options, OptionCategory category) {
    if (options == null || options.isEmpty()) return "선택 안함";

    return options.stream()
        .map(DesignOption::getCustomOption)
        .filter(opt -> opt.getCategory() == category)
        .map(opt -> opt.getOptionName())
        .findFirst()
        .orElse("선택 안함");
  }
}
