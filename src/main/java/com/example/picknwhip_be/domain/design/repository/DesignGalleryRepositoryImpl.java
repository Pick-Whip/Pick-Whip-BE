package com.example.picknwhip_be.domain.design.repository;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.entity.QDesignGallery;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
}
