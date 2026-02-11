package com.example.picknwhip_be.domain.design.repository;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.dto.res.QDesignResDTO_GalleryItemDTO; // QClass 확인 필요
import com.example.picknwhip_be.domain.design.entity.QDesignGallery;
import com.example.picknwhip_be.domain.design.enums.Style;
import com.example.picknwhip_be.domain.favorite.entity.QFavoriteDesign; // 찜 엔티티 QClass
import com.example.picknwhip_be.domain.shop.entity.QShop;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DesignGalleryRepositoryImpl implements DesignGalleryRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<DesignResDTO.DesignNameDTO> fetchDesignNamesByShopId(Long shopId) {
    QDesignGallery designGallery = QDesignGallery.designGallery;
    return queryFactory
        .select(
            Projections.constructor(
                DesignResDTO.DesignNameDTO.class, designGallery.id, designGallery.designName))
        .from(designGallery)
        .where(designGallery.shop.id.eq(shopId))
        .fetch();
  }

  @Override
  public Page<DesignResDTO.GalleryItemDTO> searchGallery(
      List<Style> categories,
      String sortType,
      String district,
      Double lat,
      Double lon,
      Long seed,
      Long userId,
      Pageable pageable) {

    QDesignGallery design = QDesignGallery.designGallery;
    QShop shop = QShop.shop;
    QFavoriteDesign favorite = QFavoriteDesign.favoriteDesign;

    BooleanExpression isMyPickCondition =
        (userId != null) ? favorite.user.userId.eq(userId) : Expressions.asBoolean(false).isTrue();

    List<DesignResDTO.GalleryItemDTO> content =
        queryFactory
            .select(
                new QDesignResDTO_GalleryItemDTO(
                    design.id,
                    design.imageUrl,
                    shop.shopName,
                    shop.address,
                    shop.minPrice,
                    shop.averageRating,
                    favorite.id))
            .from(design)
            .join(design.shop, shop)
            .leftJoin(favorite)
            .on(favorite.designGallery.eq(design).and(isMyPickCondition))
            .where(inCategories(categories), sameDistrict(shop, district))
            .orderBy(getOrderSpecifiers(sortType, shop, design, lat, lon, seed))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory
            .select(design.count())
            .from(design)
            .join(design.shop, shop)
            .where(inCategories(categories), sameDistrict(shop, district));

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private BooleanExpression inCategories(List<Style> categories) {
    if (categories == null || categories.isEmpty()) return null;
    return QDesignGallery.designGallery.keywords.any().in(categories);
  }

  private BooleanExpression sameDistrict(QShop shop, String district) {
    if (district == null || district.isBlank()) return null;
    return shop.district.eq(district);
  }

  private OrderSpecifier[] getOrderSpecifiers(
      String sortType,
      QShop shop,
      QDesignGallery design,
      Double userLat,
      Double userLon,
      Long seed) {

    List<OrderSpecifier> orders = new ArrayList<>();

    if ("NEARBY".equals(sortType) && userLat != null && userLon != null) {
      NumberTemplate<Double> distance =
          Expressions.numberTemplate(
              Double.class,
              "ST_Distance_Sphere({0}, ST_GeomFromText(CONCAT('POINT(', {2}, ' ', {1}, ')'), 4326))",
              shop.location,
              userLon,
              userLat); // {1}=userLon, {2}=userLat
      orders.add(distance.asc());

    } else if ("RATING".equals(sortType)) {
      orders.add(shop.averageRating.coalesce(0.0).desc());
    } else {
      orders.add(shop.shopName.asc());
    }

    if (seed != null) {
      orders.add(Expressions.numberTemplate(Double.class, "RAND({0})", seed).asc());
    } else {
      orders.add(Expressions.numberTemplate(Double.class, "RAND()").asc());
    }

    orders.add(design.id.asc());

    return orders.toArray(new OrderSpecifier[0]);
  }
}
