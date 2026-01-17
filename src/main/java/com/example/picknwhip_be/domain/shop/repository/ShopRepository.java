package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {}
