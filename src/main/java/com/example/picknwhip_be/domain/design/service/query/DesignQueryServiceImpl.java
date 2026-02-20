package com.example.picknwhip_be.domain.design.service.query;

import com.example.picknwhip_be.domain.S3.service.S3Service;
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
  private final S3Service s3Service;

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignListDTO findDesignListByUserId(Long userId) {

    List<FavoriteDesign> favoriteDesigns = favoriteDesignRepository.findAllByUserId(userId);

    List<DesignGallery> designs =
        favoriteDesigns.stream().map(FavoriteDesign::getDesignGallery).toList();

    DesignResDTO.GetDesignListDTO result = DesignConverter.toDesignListDTO(designs);
    List<DesignResDTO.DesignPreviewDTO> converted =
        result.getDesigns().stream()
            .map(
                item ->
                    DesignResDTO.DesignPreviewDTO.withImageUrl(
                        item, resolveImageUrl(item.getImageUrl())))
            .toList();
    return DesignResDTO.GetDesignListDTO.builder().designs(converted).build();
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

    DesignResDTO.GetDesignListDTO result = DesignConverter.toDesignListDTO(designs);
    List<DesignResDTO.DesignPreviewDTO> converted =
        result.getDesigns().stream()
            .map(
                item ->
                    DesignResDTO.DesignPreviewDTO.withImageUrl(
                        item, resolveImageUrl(item.getImageUrl())))
            .toList();
    return DesignResDTO.GetDesignListDTO.builder().designs(converted).build();
  }

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignDetailDTO findDesignDetail(Long designId) {

    DesignGallery design =
        designRepository
            .findDesignGalleryById(designId)
            .orElseThrow(() -> new DesignException(DesignErrorCode.DESIGN_NOT_FOUND));

    DesignResDTO.GetDesignDetailDTO result = DesignConverter.toDesignDetailDTO(design);
    return DesignResDTO.GetDesignDetailDTO.withImageUrl(
        result, resolveImageUrl(result.getImageUrl()));
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
      String categoryStr,
      String sortType,
      Double lat,
      Double lon,
      Long seed,
      Long userId,
      int page) {

    validateCoordinate(lat, lon);

    List<Style> categories = parseCategory(categoryStr);
    validateSortType(sortType);
    String currentDistrict = reverseGeocode(lat, lon);

    Pageable pageable = PageRequest.of(page, 4);
    Page<DesignResDTO.GalleryItemDTO> resultPage =
        designRepository.searchGallery(
            categories, sortType, currentDistrict, lat, lon, seed, userId, pageable);
    List<DesignResDTO.GalleryItemDTO> converted =
        resultPage.getContent().stream()
            .map(
                item ->
                    DesignResDTO.GalleryItemDTO.withImageUrl(
                        item, resolveImageUrl(item.getImageUrl())))
            .toList();
    return DesignResDTO.GalleryListDTO.builder()
        .currentRegion("서울시 " + (currentDistrict != null ? currentDistrict : "전체"))
        .designs(converted)
        .totalPage(resultPage.getTotalPages())
        .totalElements(resultPage.getTotalElements())
        .isFirst(resultPage.isFirst())
        .isLast(resultPage.isLast())
        .build();
  }

  private void validateCoordinate(Double lat, Double lon) {
    if (lat == null || lon == null) {
      throw new ShopException(ShopErrorCode.INVALID_COORDINATE);
    }
    if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
      throw new ShopException(ShopErrorCode.INVALID_COORDINATE);
    }
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
    throw new DesignException(DesignErrorCode.INVALID_CATEGORY);
  }

  private void validateSortType(String sortType) {
    if (sortType == null || sortType.isBlank()) return;
    if (!List.of("NAME", "NEARBY", "RATING").contains(sortType)) {
      throw new DesignException(DesignErrorCode.INVALID_SORT_TYPE);
    }
  }

  private String reverseGeocode(Double lat, Double lon) {
    if (lat >= 37.54 && lat <= 37.57 && lon >= 126.90 && lon <= 126.95) return "마포구";
    if (lat >= 37.48 && lat <= 37.52 && lon >= 127.01 && lon <= 127.05) return "강남구";
    return null;
  }

  private String resolveImageUrl(String rawImageUrl) {
    if (rawImageUrl == null || rawImageUrl.isBlank()) {
      return rawImageUrl;
    }
    if (rawImageUrl.startsWith("http://") || rawImageUrl.startsWith("https://")) {
      return rawImageUrl;
    }
    return s3Service.createPresignedDownloadUrl(rawImageUrl);
  }
}
