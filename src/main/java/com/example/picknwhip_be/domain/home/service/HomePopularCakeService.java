package com.example.picknwhip_be.domain.home.service;

import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResDTO;
import java.util.List;

public interface HomePopularCakeService {
  List<PopularCakeResDTO> getPopularCakesTop5(Long userId);
}
