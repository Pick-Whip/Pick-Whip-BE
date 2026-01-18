package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    // Projection Interface: Native Query 결과를 받기 위함
    interface ShopPreviewInfo {
        Long getShopId();
        String getShopName();
        String getShopImageUrl();
        Double getAverageRating();
        Integer getMinPrice();
        Integer getDistance();
        String getTags(); // "웨딩,기념일" 처럼 쉼표로 합쳐진 문자열로 받음
    }

    /**
     * 내 주변 가게 조회
     * 1. ST_Distance_Sphere: 거리 계산 (m)
     * 2. GROUP_CONCAT: 여러 개의 태그를 쉼표(,)로 합쳐서 하나의 문자열로 가져옴 (N+1 방지)
     * 3. LEFT JOIN: 태그가 없는 가게도 조회되어야 하므로 LEFT JOIN 사용
     */
    @Query(value = """
      SELECT s.shop_id as shopId,
             s.shop_name as shopName,
             s.shop_image_url as shopImageUrl,
             IFNULL(s.average_rating, 0.0) as averageRating,
             IFNULL(s.min_price, 0) as minPrice,
             FLOOR(ST_Distance_Sphere(s.location, POINT(:lon, :lat))) as distance,
             GROUP_CONCAT(DISTINCT k.keyword_text SEPARATOR ',') as tags
      FROM shops s
      LEFT JOIN shop_keyword_mapping m ON s.shop_id = m.shop_id
      LEFT JOIN shop_appeal_keywords k ON m.keyword_id = k.keyword_id
      WHERE s.status = 'ACTIVE'
        AND ST_Distance_Sphere(s.location, POINT(:lon, :lat)) <= :radius
      GROUP BY s.shop_id
      ORDER BY distance ASC
      """, nativeQuery = true)
    List<ShopPreviewInfo> findNearbyShops(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("radius") double radius
    );
}