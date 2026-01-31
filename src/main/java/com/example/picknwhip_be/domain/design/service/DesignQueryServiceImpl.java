package com.example.picknwhip_be.domain.design.service;

import com.example.picknwhip_be.domain.design.dto.DesignResDTO;
import com.example.picknwhip_be.domain.design.repository.DesignGalleryRepository;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteDesign;
import com.example.picknwhip_be.domain.favorite.repository.FavoriteDesignRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DesignQueryServiceImpl implements DesignQueryService {

  private final DesignGalleryRepository designRepository;
  private final FavoriteDesignRepository favoriteDesignRepository;

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignListDTO findDesignListByUserId(Long UserId) {

    List<FavoriteDesign> favoriteDesigns = favoriteDesignRepository.findAllByUserId(UserId);

    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignListDTO findDesignListByShopId(Long Id) {

    return null;
  }
}
