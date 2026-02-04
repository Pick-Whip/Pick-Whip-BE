package com.example.picknwhip_be.domain.favorite.service;

import com.example.picknwhip_be.domain.favorite.dto.req.FavoriteDesignReqDTO;

public interface FavoriteDesignService {
  FavoriteDesignReqDTO addFavoriteDesign(Long designId, Long userId);

  FavoriteDesignReqDTO deleteFavoriteDesign(Long designId, Long userId);
}
