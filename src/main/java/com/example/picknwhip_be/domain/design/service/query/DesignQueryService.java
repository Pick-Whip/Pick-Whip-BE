package com.example.picknwhip_be.domain.design.service.query;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;

public interface DesignQueryService {

  DesignResDTO.GetDesignListDTO findDesignListByUserId(Long userId);

  DesignResDTO.GetDesignListDTO findDesignListByShopId(Long shopId);

  DesignResDTO.GetDesignDetailDTO findDesignDetail(Long designId);
}
