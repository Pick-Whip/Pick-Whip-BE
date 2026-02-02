package com.example.picknwhip_be.domain.design.service;

import com.example.picknwhip_be.domain.design.converter.DesignConverter;
import com.example.picknwhip_be.domain.design.dto.DesignResDTO;
import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.design.exception.DesignException;
import com.example.picknwhip_be.domain.design.exception.code.DesignErrorCode;
import com.example.picknwhip_be.domain.design.repository.DesignGalleryRepository;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteDesign;
import com.example.picknwhip_be.domain.favorite.repository.FavoriteDesignRepository;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
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
  private final ShopRepository shopRepository;

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignListDTO findDesignListByUserId(Long userId) {

    List<FavoriteDesign> favoriteDesigns = favoriteDesignRepository.findAllByUserId(userId);

    List<DesignGallery> designs =
        favoriteDesigns.stream().map(FavoriteDesign::getDesignGallery).toList();

    return DesignConverter.toDesignListDTO(designs);
  }

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignListDTO findDesignListByShopId(Long shopId) {

    shopRepository
        .findById(shopId)
        .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

    List<DesignGallery> designs = designRepository.findByShopId(shopId);

    if (designs.isEmpty()) {
      throw new DesignException(DesignErrorCode.DESIGN_NOT_REGISTER);
    }

    return DesignConverter.toDesignListDTO(designs);
  }

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignDetailDTO findDesignDetail(Long designId) {

    DesignGallery design =
        designRepository
            .findDesignGalleryById(designId)
            .orElseThrow(() -> new DesignException(DesignErrorCode.DESIGN_NOT_FOUND));

    return DesignConverter.toDesignDetailDTO(design);
  }
}
