package com.example.picknwhip_be.domain.home.controller;

import com.example.picknwhip_be.domain.home.dto.res.PopularCakeResDTO;
import com.example.picknwhip_be.domain.home.dto.res.PopularDesignResDTO;
import com.example.picknwhip_be.domain.home.exception.code.HomeSuccessCode;
import com.example.picknwhip_be.domain.home.service.HomePopularCakeService;
import com.example.picknwhip_be.domain.home.service.HomePopularDesignService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Tag(name = "Home API", description = "홈 화면 API")
public class HomeController {

  private final HomePopularCakeService homePopularCakeService;
    private final HomePopularDesignService homePopularDesignService;

  @Operation(
      summary = "인기 케이크 Top5 조회",
      description =
          "최근 14일 동안 결제 완료(DONE) 판매량 기준 Top5 케이크(디자인)를 조회합니다. " + "집계는 매일 00:00(KST)에 스케줄러로 갱신됩니다.")
  @GetMapping("/popular-cakes/top5")
  public ApiResponse<List<PopularCakeResDTO>> getPopularCakesTop5(@ExtractPayload Long userId) {

    List<PopularCakeResDTO> result = homePopularCakeService.getPopularCakesTop5(userId);
    return ApiResponse.of(HomeSuccessCode.POPULAR_CAKES_TOP5_OK, result);
  }
    @Operation(
            summary = "인기 케이크 도안 Top4 조회",
            description = "최근 14일간 결제 완료된 주문량이 가장 많은 디자인 4개를 조회합니다. 클릭 시 상세 정보를 위한 스펙 데이터가 포함됩니다.")
    @GetMapping("/popular-designs/top4")
    public ApiResponse<List<PopularDesignResDTO>> getPopularDesigns(@ExtractPayload Long userId) {

        List<PopularDesignResDTO> result = homePopularDesignService.getPopularDesignsTop4(userId);
        return ApiResponse.of(HomeSuccessCode.POPULAR_DESIGNS_TOP4_OK, result);
    }
}
