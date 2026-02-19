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
import com.example.picknwhip_be.global.infra.kakao.client.KakaoLocalClient;
import com.example.picknwhip_be.global.infra.kakao.dto.KakaoCoord2RegionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DesignQueryServiceImpl implements DesignQueryService {

  private final DesignGalleryRepository designRepository;
  private final FavoriteDesignRepository favoriteDesignRepository;
  private final ShopRepository shopRepository;
  private final KakaoLocalClient kakaoLocalClient;
    private final S3Service s3Service;

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
    RegionResult region = resolveRegion(lat, lon);
    String currentDistrict = region.district(); // 예: "마포구"
    String currentRegionText = region.currentText(); // 예: "현재 위치: 서울시 마포구"

    Pageable pageable = PageRequest.of(page, 4);
    Page<DesignResDTO.GalleryItemDTO> resultPage =
        designRepository.searchGallery(
            categories, sortType, currentDistrict, lat, lon, seed, userId, pageable);
      List<DesignResDTO.GalleryItemDTO> converted =
              resultPage.getContent().stream()
                      .map(
                              item -> {
                                  String raw = item.getImageUrl();

                                  // 이미 완성 URL이면 그대로 (시드 데이터가 URL일 수도 있어서 안전장치)
                                  if (isHttpUrl(raw)) {
                                      return item;
                                  }

                                  // keyName이면 presigned GET URL 생성
                                  String presigned =
                                          (raw == null || raw.isBlank()) ? raw : s3Service.createPresignedDownloadUrl(raw);

                                  return DesignResDTO.GalleryItemDTO.withImageUrl(item, presigned);
                              })
                      .toList();
    return DesignResDTO.GalleryListDTO.builder()
        .currentRegion(currentRegionText)
        .designs(converted)
        .totalPage(resultPage.getTotalPages())
        .totalElements(resultPage.getTotalElements())
        .isFirst(resultPage.isFirst())
        .isLast(resultPage.isLast())
        .build();
  }

    private boolean isHttpUrl(String value) {
        if (value == null) return false;
        return value.startsWith("http://") || value.startsWith("https://");
    }

  // 좌표 -> "시/구" 텍스트 + district(구) 추출
  private RegionResult resolveRegion(Double lat, Double lon) {
    try {
      KakaoCoord2RegionResponse.Document doc = kakaoLocalClient.coordToRegion(lat, lon);
      if (doc == null) {
        return new RegionResult(null, "현재 위치: 전체"); // fallback
      }

      String region1 = shortenRegion1(doc.region_1depth_name()); // "서울특별시" -> "서울시"
      String region2 = doc.region_2depth_name(); // "마포구"

      String district = (region2 == null || region2.isBlank()) ? null : region2;

      String text =
          (district == null)
              ? "현재 위치: " + (region1 != null ? region1 : "전체")
              : "현재 위치: " + (region1 != null ? region1 + " " + district : district);

      return new RegionResult(district, text);

    } catch (Exception e) {
      log.warn("Kakao reverse-geocode failed. lat={}, lon={}, reason={}", lat, lon, e.getMessage());
      // 홈이 죽지 않게 fallback
      return new RegionResult(null, "현재 위치: 전체");
    }
  }

  private String shortenRegion1(String region1) {
    if (region1 == null) return null;
    if (region1.startsWith("서울")) return "서울시";
    if (region1.endsWith("특별시")) return region1.replace("특별시", "시");
    if (region1.endsWith("광역시")) return region1.replace("광역시", "시");
    return region1;
  }

  private record RegionResult(String district, String currentText) {}

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
}
