package com.example.picknwhip_be.global.infra.kakao.client;

import com.example.picknwhip_be.global.infra.kakao.dto.KakaoCoord2RegionResponse;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    private final RestTemplate restTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    public KakaoCoord2RegionResponse.Document coordToRegion(Double lat, Double lon) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json")
                .queryParam("x", lon) // Kakao는 x=경도(lon), y=위도(lat)
                .queryParam("y", lat)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey); // client-id(REST API 키) 사용
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoCoord2RegionResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, KakaoCoord2RegionResponse.class);

        KakaoCoord2RegionResponse body = response.getBody();
        if (body == null || body.documents() == null || body.documents().isEmpty()) return null;

        // "H"(행정동)을 우선 선택
        return body.documents().stream()
                .sorted(Comparator.comparing((KakaoCoord2RegionResponse.Document d) -> !"H".equals(d.region_type())))
                .findFirst()
                .orElse(null);
    }
}