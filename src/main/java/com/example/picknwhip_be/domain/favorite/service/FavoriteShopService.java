package com.example.picknwhip_be.domain.favorite.service;

import com.example.picknwhip_be.domain.favorite.dto.res.FavoriteShopResponse;
import com.example.picknwhip_be.domain.favorite.entity.FavoriteShop;
import com.example.picknwhip_be.domain.favorite.exception.FavoriteShopException;
import com.example.picknwhip_be.domain.favorite.repository.FavoriteShopRepository;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.repository.UserRepository; // 유저 리포지토리 필요
import lombok.RequiredArgsConstructor;
import static com.example.picknwhip_be.domain.favorite.exception.code.FavoriteShopErrorCode.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteShopService {

    private final FavoriteShopRepository favoriteShopRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository; // 유저 조회를 위해 필요

    /**
     * 마이픽 가게 등록
     */
    public FavoriteShopResponse addFavoriteShop(Long userId, Long shopId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FavoriteShopException(USER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new FavoriteShopException(SHOP_NOT_FOUND));

        if (favoriteShopRepository.existsByUserAndShop(user, shop)) {
            throw new FavoriteShopException(FAVORITE_ALREADY_EXISTS);
        }

        favoriteShopRepository.save(FavoriteShop.create(user, shop));

        return new FavoriteShopResponse(shopId, true);
    }

    /**
     * 마이픽 가게 취소
     */
    public FavoriteShopResponse removeFavoriteShop(Long userId, Long shopId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FavoriteShopException(USER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new FavoriteShopException(SHOP_NOT_FOUND));

        FavoriteShop favoriteShop = favoriteShopRepository.findByUserAndShop(user, shop)
                .orElseThrow(() -> new FavoriteShopException(FAVORITE_NOT_FOUND));

        favoriteShopRepository.delete(favoriteShop);

        return new FavoriteShopResponse(shopId, false);
    }
}