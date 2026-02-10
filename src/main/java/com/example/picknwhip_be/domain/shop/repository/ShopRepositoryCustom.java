package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.dto.req.ShopReqDTO;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopRepositoryCustom {
  Page<Shop> searchShops(ShopReqDTO.ShopSearchCondition condition, Pageable pageable);
}
