package com.example.picknwhip_be.global.infra.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoCoord2RegionResponse(List<Document> documents) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Document(
      String region_type, // "H"(행정동) / "B"(법정동)
      String region_1depth_name, // 시/도
      String region_2depth_name, // 시/군/구 (서울이면 "마포구")
      String region_3depth_name, // 읍/면/동
      String address_name) {}
}
