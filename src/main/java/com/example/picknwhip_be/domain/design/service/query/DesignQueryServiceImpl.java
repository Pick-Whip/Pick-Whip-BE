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

        if (lat != null || lon != null) {
            validateCoordinate(lat, lon);
        }

        if ("NEARBY".equals(sortType) && (lat == null || lon == null)) {
            throw new ShopException(ShopErrorCode.INVALID_COORDINATE);
        }

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
            return null; // 필터링 안 함
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
        if (lat == null || lon == null) return null;
        // TODO: 실제 Naver Map API 연동 필요
        // 현재는 테스트를 위해 요청이 들어오면 무조건 특정 값을 반환하거나,
        // 클라이언트가 lat/lon과 함께 region 이름을 보내주는 것이 성능상 유리함.
        // 여기서는 로직 동작 확인을 위해 임시로 null 반환 (전체 구 조회)
        // "마포구" 등으로 하드코딩하면 테스트 가능.
        return null;
    }
}
