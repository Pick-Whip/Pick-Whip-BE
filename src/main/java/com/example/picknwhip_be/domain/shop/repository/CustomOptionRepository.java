package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.CustomOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomOptionRepository extends JpaRepository<CustomOption, Long> {

  List<CustomOption> findByShopId(Long shopId);
}
