package com.example.picknwhip_be.domain.shop.repository;

import static com.example.picknwhip_be.domain.design.entity.QDesignGallery.designGallery;
import static com.example.picknwhip_be.domain.shop.entity.QShop.shop;

import com.example.picknwhip_be.domain.shop.dto.res.ShopReqDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepository.ShopRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Shop> searchShopByDynamicFilter(ShopReqDTO.ShopSearchReqDTO condition) {

    return queryFactory
        .selectFrom(shop)
        .distinct()
        .leftJoin(shop.designGalleries, designGallery)
        .where(
            containsKeyword(condition.getKeyword()),
            eqRegion(condition.getRegion()),
            betweenPrice(condition.getMinPrice(), condition.getMaxPrice()),
            inStyles(condition.getStyles()))
        .fetch();
  }

  private BooleanExpression containsKeyword(String keyword) {
    if (keyword == null || keyword.isBlank()) return null;
    return shop.shopName
        .contains(keyword)
        .or(shop.description.contains(keyword))
        .or(designGallery.designName.contains(keyword));
  }

  // 지역 필터
  private BooleanExpression eqRegion(String region) {
    if (region == null || region.isBlank()) return null;

    return shop.address
        .region1DepthName
        .contains(region)
        .or(shop.address.region2DepthName.contains(region));
  }

  // 가격 범위 필터
  private BooleanExpression betweenPrice(Integer min, Integer max) {
    if (min == null && max == null) return null;

    // min 이상 max 이하 (둘 중 하나만 있어도 동작하도록)
    BooleanExpression result = null;
    if (min != null) result = designGallery.basePrice.goe(min); // >= min
    if (max != null) {
      BooleanExpression maxCondition = designGallery.basePrice.loe(max); // <= max
      result = (result == null) ? maxCondition : result.and(maxCondition);
    }
    return result;
  }

  // 스타일 태그 다중 검색
  private BooleanBuilder inStyles(List<String> styles) {
    if (styles == null || styles.isEmpty()) return null;

    BooleanBuilder builder = new BooleanBuilder();
    for (String style : styles) {
      builder.or(designGallery.description.contains(style));
    }
    return builder;
  }
}
