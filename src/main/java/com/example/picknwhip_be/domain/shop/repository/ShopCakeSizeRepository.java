package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopCakeSizeRepository extends JpaRepository<ShopCakeSize, Long> {

  List<ShopCakeSize> findByShopId(Long shopId);
}
