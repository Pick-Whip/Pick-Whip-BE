package com.example.picknwhip_be.domain.design.service.query;

import com.example.picknwhip_be.domain.design.converter.DesignConverter;
import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.design.enums.Style;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    @Transactional
    public DesignResDTO.DesignListDTO findShopDesignNameList(Long shopId) {
        if (!shopRepository.existsById(shopId)) {
            throw new ShopException(ShopErrorCode.SHOP_NOT_FOUND);
        }
        List<DesignResDTO.DesignNameDTO> items = designRepository.fetchDesignNamesByShopId(shopId);
        return DesignConverter.toShopDesignNameListDTO(items);
    }

    @Override
    @Transactional(readOnly = true)
    public DesignResDTO.GalleryListDTO searchGallery(
            String categoryStr, String sortType, Double lat, Double lon, Long userId, int page) {

        List<Style> categories = parseCategory(categoryStr);

        validateCoordinate(lat, lon);

        // TODO: 외부 지도 API 연동 시, API 호출 실패 등에 대한 예외처리도 이곳에서 try-catch로 감싸야 함
        String currentDistrict = reverseGeocode(lat, lon);

        Pageable pageable = PageRequest.of(page, 4);

        Page<DesignResDTO.GalleryItemDTO> resultPage = designRepository.searchGallery(
                categories, sortType, currentDistrict, lat, lon, userId, pageable
        );

        return DesignResDTO.GalleryListDTO.builder()
                .currentRegion("서울시 " + (currentDistrict != null ? currentDistrict : "전체"))
                .designs(resultPage.getContent())
                .totalPage(resultPage.getTotalPages())
                .totalElements(resultPage.getTotalElements())
                .isFirst(resultPage.isFirst())
                .isLast(resultPage.isLast())
                .build();
    }

    private List<Style> parseCategory(String categoryStr) {
        if ("전체".equals(categoryStr) || categoryStr == null || categoryStr.isBlank()) {
            return null;
        }
        for (Style style : Style.values()) {
            if (style.getLabel().equals(categoryStr)) {
                return List.of(style);
            }
        }
        return null;
    }

    private void validateCoordinate(Double lat, Double lon) {
        if (lat == null || lon == null) return;
        if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
            throw new ShopException(ShopErrorCode.INVALID_COORDINATE);
        }
    }

    // 역지오코딩 (좌표 -> 주소) 시뮬레이션
    private String reverseGeocode(Double lat, Double lon) {
        if (lat >= 37.54 && lat <= 37.57 && lon >= 126.90 && lon <= 126.95) {
            return "마포구";
        }
        if (lat >= 37.48 && lat <= 37.52 && lon >= 127.01 && lon <= 127.05) {
            return "강남구";
        }
        return "마포구";
    }
}
