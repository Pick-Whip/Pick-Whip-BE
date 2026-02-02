package com.example.picknwhip_be.domain.design.controller;

import com.example.picknwhip_be.domain.design.dto.res.DesignResDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Design API", description = "디자인 갤러리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/design")
public class DesignController {

  @GetMapping("/{userId}")
  public DesignResDTO.GetDesignListDTO getDesignListByUserId(@PathVariable Long userId) {

    return null;
  }

  @GetMapping("/{shopId}")
  public DesignResDTO.GetDesignListDTO getDesignListByShopId(@PathVariable Long shopId) {

    return null;
  }
}
