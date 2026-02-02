package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.Shop;
import java.util.List;
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

  @Query(
      value =
          "SELECT * FROM shops s "
              + "WHERE s.status = 'ACTIVE' "
              + "AND ST_SRID(s.location) = 4326 "
              + "AND MBRContains(ST_SRID(ST_MakeEnvelope(POINT(:lowLon, :lowLat), POINT(:highLon, :highLat)), 4326), s.location)",
      nativeQuery = true)
  List<Shop> findShopsInBoundary(
      @Param("lowLat") Double lowLat,
      @Param("highLat") Double highLat,
      @Param("lowLon") Double lowLon,
      @Param("highLon") Double highLon);
}
