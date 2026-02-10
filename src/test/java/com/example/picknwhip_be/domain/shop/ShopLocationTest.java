package com.example.picknwhip_be.domain.shop;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Tag("mysql")
@ActiveProfiles("mysqltest")
@EnabledIfEnvironmentVariable(named = "MYSQL_TEST_URL", matches = ".+")
@EnabledIfEnvironmentVariable(named = "MYSQL_TEST_USERNAME", matches = ".+")
@EnabledIfEnvironmentVariable(named = "MYSQL_TEST_PASSWORD", matches = ".+")
@SpringBootTest
class ShopLocationTest {

  @Autowired private ShopRepository shopRepository;

  @Test
  @DisplayName("가게 위치(Point)가 DB에 정상적으로 저장되고 조회되어야 한다")
  @Transactional
  void saveAndFindPointTest() {
    Point location = point4326(127.027610, 37.498095); // lng, lat

    Shop shop = new Shop(null, "테스트 케이크샵", "010-1234-5678", "서울시 테헤란로", location, "강남구");
    Shop savedShop = shopRepository.save(shop);

    Shop foundShop = shopRepository.findById(savedShop.getId()).orElseThrow();

    assertThat(foundShop.getLocation().getX()).isEqualTo(127.027610);
    assertThat(foundShop.getLocation().getY()).isEqualTo(37.498095);
    assertThat(foundShop.getDistrict()).isEqualTo("강남구");
  }

  private static Point point4326(double lng, double lat) {
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    return geometryFactory.createPoint(new Coordinate(lng, lat));
  }
}
