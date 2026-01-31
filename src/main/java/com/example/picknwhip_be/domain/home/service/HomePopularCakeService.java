package com.example.picknwhip_be.domain.home.service;

import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResponseDto;
import java.util.List;

public interface HomePopularCakeService {
  List<PopularCakeResponseDto> getPopularCakesTop5(Long userId);
}
