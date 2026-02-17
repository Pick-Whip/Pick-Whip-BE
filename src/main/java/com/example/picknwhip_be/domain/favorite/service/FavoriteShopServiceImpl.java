package com.example.picknwhip_be.domain.favorite.service;

import static com.example.picknwhip_be.domain.favorite.exception.code.FavoriteErrorCode.*;

import com.example.picknwhip_be.domain.favorite.converter.FavoriteShopConverter;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopDTO;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopListResDTO;
import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResDTO;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteShop;
import com.example.picknwhip_be.domain.favorite.exception.FavoriteException;
import com.example.picknwhip_be.domain.favorite.repository.FavoriteShopRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteShopServiceImpl implements FavoriteShopService {

  private final FavoriteShopRepository favoriteShopRepository;
  private final ShopRepository shopRepository;
  private final UserRepository userRepository;

  /** 마이픽 가게 등록 */
  @Override
  public FavoriteShopResDTO addFavoriteShop(Long userId, Long shopId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    Shop shop =
        shopRepository.findById(shopId).orElseThrow(() -> new FavoriteException(SHOP_NOT_FOUND));

    if (favoriteShopRepository.existsByUserAndShop(user, shop)) {
      throw new FavoriteException(FAVORITE_ALREADY_EXISTS);
    }

    favoriteShopRepository.save(FavoriteShop.create(user, shop));

    return new FavoriteShopResDTO(shopId, true);
  }

  /** 마이픽 가게 취소 */
  @Override
  public FavoriteShopResDTO removeFavoriteShop(Long userId, Long shopId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    Shop shop =
        shopRepository.findById(shopId).orElseThrow(() -> new FavoriteException(SHOP_NOT_FOUND));

    FavoriteShop favoriteShop =
        favoriteShopRepository
            .findByUserAndShop(user, shop)
            .orElseThrow(() -> new FavoriteException(FAVORITE_NOT_FOUND));

    favoriteShopRepository.delete(favoriteShop);

    return new FavoriteShopResDTO(shopId, false);
  }

  /** 마이픽 가게 목록 조회 (커서 페이징) */
  @Override
  @Transactional(readOnly = true)
  public FavoriteShopListResDTO getMyPickShops(Long userId, Long cursor, int limit) {
    if (limit <= 0) {
      throw new FavoriteException(INVALID_PAGE_SIZE);
    }
    if (!userRepository.existsById(userId)) {
      throw new UserException(UserErrorCode.USER_NOT_FOUND);
    }

    List<Long> rawIds =
        favoriteShopRepository.findIdsByUserIdAndCursor(
            userId, cursor, PageRequest.of(0, limit + 1));

    boolean hasNext = false;
    List<Long> pageIds = new ArrayList<>(rawIds);

    if (pageIds.size() > limit) {
      hasNext = true;
      pageIds = pageIds.subList(0, limit);
    }

    List<FavoriteShop> favoriteShops =
        pageIds.isEmpty()
            ? List.of()
            : favoriteShopRepository.findAllByIdsWithShopAndKeywords(pageIds);

    List<FavoriteShopDTO> shopDtos =
        favoriteShops.stream().map(FavoriteShopConverter::toDto).collect(Collectors.toList());

    Long nextCursor = null;
    if (hasNext && !pageIds.isEmpty()) {
      nextCursor = pageIds.get(pageIds.size() - 1);
    }

    return FavoriteShopConverter.toListResponse(shopDtos, nextCursor, hasNext);
  }
}
