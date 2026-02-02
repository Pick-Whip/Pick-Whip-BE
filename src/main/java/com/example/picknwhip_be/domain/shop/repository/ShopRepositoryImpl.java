package com.example.picknwhip_be.domain.shop.repository;

import static com.example.picknwhip_be.domain.design.entity.QDesignGallery.designGallery;
import static com.example.picknwhip_be.domain.shop.entity.QShop.shop;

import com.example.picknwhip_be.domain.design.entity.QDesignGallery;
import com.example.picknwhip_be.domain.shop.dto.res.ShopReqDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLSubQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Tuple> searchShopByDynamicFilter(ShopReqDTO.ShopSearchReqDTO condition) {

    NumberTemplate<Double> distanceExpression =
        Expressions.numberTemplate(
            Double.class,
            "ST_Distance_Sphere({0}, {1})",
            shop.location,
            createPoint(condition.getLat(), condition.getLon()));

    QDesignGallery designSub = new QDesignGallery("designSub");

    JPQLSubQuery<Integer> minPriceSub =
        JPAExpressions.select(designSub.basePrice.min().coalesce(0)) // 없으면 0
            .from(designSub)
            .where(designSub.shop.eq(shop));

    JPQLSubQuery<Integer> maxPriceSub =
        JPAExpressions.select(designSub.basePrice.max().coalesce(0))
            .from(designSub)
            .where(designSub.shop.eq(shop));

    return queryFactory
        .select(shop, distanceExpression, minPriceSub, maxPriceSub)
        .from(shop)
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
      builder.or(designGallery.keywords.contains(style));
      builder.or(designGallery.description.contains(style));
    }
    return builder;
  }

  private com.querydsl.core.types.Expression createPoint(Double lat, Double lon) {
    if (lat == null || lon == null) {
      return Expressions.nullExpression();
    }
    return Expressions.stringTemplate(
        "ST_GeomFromText({0}, 4326)", "POINT(" + lon + " " + lat + ")");
  }
}
