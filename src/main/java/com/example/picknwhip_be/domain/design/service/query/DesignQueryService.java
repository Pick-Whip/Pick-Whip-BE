package com.example.picknwhip_be.domain.design.service.query;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;

public interface DesignQueryService {

  DesignResDTO.GetDesignListDTO findDesignListByUserId(Long id);

  DesignResDTO.GetDesignListDTO findDesignListByShopId(Long id);
}
