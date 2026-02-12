package com.example.picknwhip_be.domain.shop.repository;

import static com.example.picknwhip_be.domain.design.entity.QDesignGallery.designGallery;
import static com.example.picknwhip_be.domain.shop.entity.QShop.shop;

import com.example.picknwhip_be.domain.design.enums.Purpose;
import com.example.picknwhip_be.domain.design.enums.Style;
import com.example.picknwhip_be.domain.shop.dto.req.ShopReqDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.enums.Area;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Shop> searchShops(ShopReqDTO.ShopSearchCondition condition, Pageable pageable) {

    // 1. 컨텐츠 조회 쿼리
    List<Shop> content =
        queryFactory
            .selectFrom(shop)
            .distinct()
            .leftJoin(shop.designGalleries, designGallery)
            .where(
                containsKeyword(condition.getKeyword()), // 검색어
                inStyles(condition.getStyles()), // 스타일/맛/토핑 필터
                inPurposes(condition.getPurposes()), // 용도 필터
                betweenPrice(condition.getMinPrice(), condition.getMaxPrice()), // 가격대
                filterLocation(condition.getCity(), condition.getSubAreas()) // 지역
                )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable))
            .fetch();

    // 2. 카운트 쿼리
    JPAQuery<Long> countQuery =
        queryFactory
            .select(shop.countDistinct())
            .from(shop)
            .leftJoin(shop.designGalleries, designGallery)
            .where(
                containsKeyword(condition.getKeyword()),
                inStyles(condition.getStyles()),
                inPurposes(condition.getPurposes()),
                betweenPrice(condition.getMinPrice(), condition.getMaxPrice()),
                filterLocation(condition.getCity(), condition.getSubAreas()));

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private OrderSpecifier[] getOrderSpecifiers(Pageable pageable) {
    List<OrderSpecifier> orders = new ArrayList<>();

    if (pageable.getSort() != null && !pageable.getSort().isEmpty()) {
      for (Sort.Order order : pageable.getSort()) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        String prop = order.getProperty();
        if (!isValidProperty(prop)) {
          continue;
        }
        PathBuilder<Shop> orderByExpression = new PathBuilder<>(Shop.class, "shop");
        orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
      }
    } else {
      // 기본 정렬 조건
      orders.add(new OrderSpecifier(Order.DESC, shop.createdAt));
    }

    return orders.toArray(new OrderSpecifier[0]);
  }

  private boolean isValidProperty(String prop) {
    return List.of("createdAt", "shopName", "minPrice", "maxPrice", "averageRating").contains(prop);
  }

  // 1. 통합 검색어 (가게이름, 주소, 디자인이름)
  private BooleanExpression containsKeyword(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return null;
    }
    return shop.shopName
        .contains(keyword)
        .or(shop.address.contains(keyword)) // 주소 검색 추가
        .or(designGallery.designName.contains(keyword));
  }

  // 2. 스타일 검색

  private BooleanExpression inStyles(List<Style> styles) {
    if (styles == null || styles.isEmpty()) {
      return null;
    }

    return designGallery.keywords.any().in(styles);
  }

  // 3. 용도 필터링

  private BooleanExpression inPurposes(List<Purpose> purposes) {
    if (purposes == null || purposes.isEmpty()) {
      return null;
    }

    return designGallery.purposes.any().in(purposes);
  }

  // 4. 가격대 필터링

  private BooleanExpression betweenPrice(Integer userMin, Integer userMax) {
    if (userMin == null && userMax == null) {
      return null;
    }

    BooleanExpression loeMax = (userMax != null) ? shop.minPrice.loe(userMax) : null;
    BooleanExpression goeMin = (userMin != null) ? shop.maxPrice.goe(userMin) : null;

    return goeAll(loeMax, goeMin);
  }

  // 5. 지역 필터링
  private BooleanExpression filterLocation(String city, List<String> subAreas) {
    if (city == null || city.isBlank()) {
      return null;
    }

    BooleanExpression cityCondition = shop.address.contains(city);

    // 세부 지역을 선택하지 않으면 시/도 만 검색
    if (subAreas == null || subAreas.isEmpty()) {
      return cityCondition;
    }

    // 세부 지역 선택 시 해당 지역의 키워드들을 OR 조건으로 묶음
    BooleanExpression subAreaCondition = null;

    for (String subAreaName : subAreas) {
      Area area = Area.findBySubRegionName(subAreaName);

      if (area != null) {
        for (String keyword : area.getSearchKeywords()) {
          BooleanExpression matchKeyword = shop.address.contains(keyword);
          subAreaCondition =
              (subAreaCondition == null) ? matchKeyword : subAreaCondition.or(matchKeyword);
        }
      }
    }

    return (subAreaCondition != null) ? cityCondition.and(subAreaCondition) : cityCondition;
  }

  // BooleanExpression 조합용 헬퍼
  private BooleanExpression goeAll(BooleanExpression... expressions) {
    BooleanExpression result = null;
    for (BooleanExpression expr : expressions) {
      if (expr != null) {
        result = (result == null) ? expr : result.and(expr);
      }
    }
    return result;
  }
}
