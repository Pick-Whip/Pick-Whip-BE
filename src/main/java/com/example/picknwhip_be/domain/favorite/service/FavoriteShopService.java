package com.example.picknwhip_be.domain.favorite.service;

import static com.example.picknwhip_be.domain.favorite.exception.code.FavoriteShopErrorCode.*;

import com.example.picknwhip_be.domain.favorite.converter.FavoriteShopConverter;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopDto;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopListResponse;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResponse;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteShop;
import com.example.picknwhip_be.domain.favorite.exception.FavoriteShopException;
import com.example.picknwhip_be.domain.favorite.repository.FavoriteShopRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteShopService {

  private final FavoriteShopRepository favoriteShopRepository;
  private final ShopRepository shopRepository;
  private final UserRepository userRepository; // 유저 조회를 위해 필요

  /** 마이픽 가게 등록 */
  public FavoriteShopResponse addFavoriteShop(Long userId, Long shopId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    Shop shop =
        shopRepository
            .findById(shopId)
            .orElseThrow(() -> new FavoriteShopException(SHOP_NOT_FOUND));

    if (favoriteShopRepository.existsByUserAndShop(user, shop)) {
      throw new FavoriteShopException(FAVORITE_ALREADY_EXISTS);
    }

    favoriteShopRepository.save(FavoriteShop.create(user, shop));

    return new FavoriteShopResponse(shopId, true);
  }

  /** 마이픽 가게 취소 */
  public FavoriteShopResponse removeFavoriteShop(Long userId, Long shopId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    Shop shop =
        shopRepository
            .findById(shopId)
            .orElseThrow(() -> new FavoriteShopException(SHOP_NOT_FOUND));

    FavoriteShop favoriteShop =
        favoriteShopRepository
            .findByUserAndShop(user, shop)
            .orElseThrow(() -> new FavoriteShopException(FAVORITE_NOT_FOUND));

    favoriteShopRepository.delete(favoriteShop);

    return new FavoriteShopResponse(shopId, false);
  }

  /** 마이픽 가게 목록 조회 (커서 페이징) */
  @Transactional(readOnly = true)
  public FavoriteShopListResponse getMyPickShops(Long userId, Long cursor, int limit) {
    // 유저 검증
    if (!userRepository.existsById(userId)) {
      throw new UserException(UserErrorCode.USER_NOT_FOUND);
    }

    // 데이터 조회 (limit + 1개 조회)
    List<FavoriteShop> favoriteShops =
        favoriteShopRepository.findAllByUserIdAndCursor(
            userId, cursor, PageRequest.of(0, limit + 1));

    // hasNext 판단
    boolean hasNext = false;
    if (favoriteShops.size() > limit) {
      hasNext = true;
      favoriteShops.remove(limit);
    }

    // DTO 변환
    List<FavoriteShopDto> shopDtos =
        favoriteShops.stream().map(FavoriteShopConverter::toDto).collect(Collectors.toList());
    Long nextCursor = null;
    if (!favoriteShops.isEmpty()) {
      nextCursor = favoriteShops.get(favoriteShops.size() - 1).getId();
    }

    return FavoriteShopConverter.toListResponse(shopDtos, nextCursor, hasNext);
  }
}
