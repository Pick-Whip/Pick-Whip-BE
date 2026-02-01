package com.example.picknwhip_be.domain.home.service;

import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResponseDTO;
import java.util.List;

public interface HomePopularCakeService {
  List<PopularCakeResponseDTO> getPopularCakesTop5(Long userId);
}
