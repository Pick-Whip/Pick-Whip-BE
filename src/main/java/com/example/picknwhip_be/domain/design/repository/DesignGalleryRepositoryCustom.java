package com.example.picknwhip_be.domain.design.repository;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import java.util.List;

public interface DesignGalleryRepositoryCustom {
  List<DesignResDTO.DesignNameDTO> fetchDesignNamesByShopId(Long shopId);
}
