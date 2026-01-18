package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    @Query(value = """
      SELECT s.shop_id as shopId,
             s.shop_name as shopName,
             s.shop_image_url as shopImageUrl,
             IFNULL(s.average_rating, 0.0) as averageRating,
             IFNULL(s.min_price, 0) as minPrice,
             FLOOR(ST_Distance_Sphere(
                 s.location,
                 ST_SRID(POINT(:lon, :lat), 4326)
             )) as distance,
             GROUP_CONCAT(DISTINCT k.keyword_text SEPARATOR ',') as tags
      FROM shops s
      LEFT JOIN shop_keyword_mapping m ON s.shop_id = m.shop_id
      LEFT JOIN shop_appeal_keywords k ON m.keyword_id = k.keyword_id
      WHERE s.status = 'ACTIVE'
        -- 1차: 바운딩 박스(MBR)로 후보 압축 (Spatial Index 타기 좋음)
        AND MBRContains(
            ST_MakeEnvelope(:minLon, :minLat, :maxLon, :maxLat, 4326),
            s.location
        )
        -- 2차: 정확한 원형 반경 필터
        AND ST_Distance_Sphere(
            s.location,
            ST_SRID(POINT(:lon, :lat), 4326)
        ) <= :radius
      GROUP BY s.shop_id
      ORDER BY distance ASC
      """, nativeQuery = true)
    List<ShopPreviewInfo> findNearbyShops(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("radius") double radius
    );
}
