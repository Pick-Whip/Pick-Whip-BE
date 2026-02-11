package com.example.picknwhip_be.domain.home.service;

import com.example.picknwhip_be.domain.home.dto.res.PopularDesignResDTO;
import java.util.List;

public interface HomePopularDesignService {
  /**
   * 인기 디자인 Top4 조회
   *
   * @param userId 로그인한 유저 ID (찜 여부 확인용, 비로그인 시 null)
   * @return 상세 정보가 포함된 디자인 리스트
   */
  List<PopularDesignResDTO> getPopularDesignsTop4(Long userId);
}
