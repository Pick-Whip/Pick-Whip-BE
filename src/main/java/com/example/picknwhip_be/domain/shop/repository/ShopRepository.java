package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.Shop;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopRepository extends JpaRepository<Shop, Long> {

  interface ShopPreviewInfo {
    Long getShopId();

    String getShopName();

    String getShopImageUrl();

    Double getAverageRating();

    Integer getMinPrice();

    Integer getDistance();

    String getTags();
  }
    // 상세 조회용
    interface ShopDetailInfo {
        Long getShopId();
        String getShopName();
        String getShopImageUrl();
        Double getAverageRating();
        Integer getReviewCount();
        Double getDistance();
        String getAddress();
        String getPhone();
        String getKeywords();
    }

  @Query(
      value =
          """
      SELECT s.shop_id as shopId,
             s.shop_name as shopName,
             s.shop_image_url as shopImageUrl,
             IFNULL(s.average_rating, 0.0) as averageRating,
             IFNULL(s.min_price, 0) as minPrice,
             FLOOR(
               ST_Distance_Sphere(
                 s.location,
                 ST_SRID(POINT(:lon, :lat), 4326)
               )
             ) as distance,
             COALESCE(JSON_ARRAYAGG(kt.keyword_text), JSON_ARRAY()) as tags
      FROM shops s
      LEFT JOIN (
          SELECT DISTINCT m.shop_id, k.keyword_text
          FROM shop_keyword_mapping m
          JOIN shop_appeal_keywords k ON m.keyword_id = k.keyword_id
      ) kt ON s.shop_id = kt.shop_id
      WHERE s.status = 'ACTIVE'
        AND ST_SRID(s.location) = 4326
        AND MBRWithin(
          s.location,
          ST_SRID(
            ST_MakeEnvelope(POINT(:minLon, :minLat), POINT(:maxLon, :maxLat)),
            4326
          )
        )
        AND ST_Distance_Sphere(
              s.location,
              ST_SRID(POINT(:lon, :lat), 4326)
            ) <= :radius
      GROUP BY s.shop_id
      ORDER BY distance ASC
      LIMIT :limit
      """,
      nativeQuery = true)
  List<ShopPreviewInfo> findNearbyShops(
      @Param("lat") double lat,
      @Param("lon") double lon,
      @Param("minLat") double minLat,
      @Param("maxLat") double maxLat,
      @Param("minLon") double minLon,
      @Param("maxLon") double maxLon,
      @Param("radius") double radius,
      @Param("limit") int limit);

    // 가게 상세 조회
    @Query(value = """
      SELECT s.shop_id as shopId,
             s.shop_name as shopName,
             s.shop_image_url as shopImageUrl,
             IFNULL(s.average_rating, 0.0) as averageRating,
             (SELECT COUNT(*) FROM review r WHERE r.shop_id = s.shop_id AND r.deleted_at IS NULL) as reviewCount,
             ST_Distance_Sphere(s.location, ST_SRID(POINT(:lon, :lat), 4326)) as distance,
             s.address as address,
             s.phone as phone,
             COALESCE(JSON_ARRAYAGG(kt.keyword_text), JSON_ARRAY()) as keywords
      FROM shops s
      LEFT JOIN (
          SELECT DISTINCT m.shop_id, k.keyword_text
          FROM shop_keyword_mapping m
          JOIN shop_appeal_keywords k ON m.keyword_id = k.keyword_id
      ) kt ON s.shop_id = kt.shop_id
      WHERE s.shop_id = :shopId AND s.status = 'ACTIVE'
      GROUP BY s.shop_id
      """, nativeQuery = true)
    Optional<ShopDetailInfo> findShopDetailById(@Param("shopId") Long shopId, @Param("lat") double lat, @Param("lon") double lon);
}


