package com.example.picknwhip_be.domain.shop.repository;

import com.example.picknwhip_be.domain.shop.dto.res.ShopReqDTO;
import com.querydsl.core.Tuple;
import java.util.List;

public interface ShopRepositoryCustom {
  List<Tuple> searchShopByDynamicFilter(ShopReqDTO.ShopSearchReqDTO condition);
}
