package com.example.picknwhip_be.domain.design.service;

import com.example.picknwhip_be.domain.design.dto.DesignResDTO;

public interface DesignQueryService {

  DesignResDTO.GetDesignListDTO findDesignListByUserId(Long id);

  DesignResDTO.GetDesignListDTO findDesignListByShopId(Long id);
}
