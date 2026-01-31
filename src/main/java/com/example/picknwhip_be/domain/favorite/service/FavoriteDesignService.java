package com.example.picknwhip_be.domain.favorite.service;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.design.exception.DesignException;
import com.example.picknwhip_be.domain.design.exception.code.DesignErrorCode;
import com.example.picknwhip_be.domain.design.repository.DesignGalleryRepository;
import com.example.picknwhip_be.domain.favorite.dto.req.FavoriteDesignResponse;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteDesign;
import com.example.picknwhip_be.domain.favorite.exception.FavoriteException;
import com.example.picknwhip_be.domain.favorite.exception.code.FavoriteErrorCode;
import com.example.picknwhip_be.domain.favorite.repository.FavoriteDesignRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteDesignService {

  private final UserRepository userRepository;
  private final DesignGalleryRepository designRepository;
  private final FavoriteDesignRepository favoriteDesignRepository;

  public FavoriteDesignResponse addFavoriteDesign(Long designID, Long userID) {

    User user =
        userRepository
            .findById(userID)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    DesignGallery design =
        designRepository
            .findById(designID)
            .orElseThrow(() -> new DesignException(DesignErrorCode.DESIGN_NOT_FOUND));

    if (favoriteDesignRepository.existsByUserAndDesignGallery(user, design)) {
      throw new FavoriteException(FavoriteErrorCode.DESIGN_ALREADY_EXISTS);
    }

    favoriteDesignRepository.save(
        FavoriteDesign.builder().user(user).designGallery(design).build());

    return new FavoriteDesignResponse(designID, true);
  }

  public FavoriteDesignResponse deleteFavoriteDesign(Long designID, Long userID) {

    User user =
        userRepository
            .findById(userID)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    DesignGallery design =
        designRepository
            .findById(designID)
            .orElseThrow(() -> new DesignException(DesignErrorCode.DESIGN_NOT_FOUND));

    FavoriteDesign favoriteDesign =
        favoriteDesignRepository
            .findByUserAndDesignGallery(user, design)
            .orElseThrow(() -> new FavoriteException(FavoriteErrorCode.FAVORITE_NOT_FOUND));

    favoriteDesignRepository.delete(favoriteDesign);

    return new FavoriteDesignResponse(designID, false);
  }
}
