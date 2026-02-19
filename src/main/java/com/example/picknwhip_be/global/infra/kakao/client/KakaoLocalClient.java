package com.example.picknwhip_be.global.infra.kakao.client;

import com.example.picknwhip_be.global.infra.kakao.dto.KakaoCoord2RegionResponse;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    private final RestTemplate restTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    public KakaoCoord2RegionResponse.Document coordToRegion(Double lat, Double lon) {

        String url = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json")
                .queryParam("x", lon) // x=경도
                .queryParam("y", lat) // y=위도
                .build()
                .toUriString();

        // 환경변수/프로퍼티에 공백/개행이 섞이는 경우가 진짜 흔함 → 무조건 trim
        String appKey = (kakaoRestApiKey == null) ? null : kakaoRestApiKey.trim();

        // 키 원문 노출 금지: 길이만 비교(원본 vs trim)
        int rawLen = (kakaoRestApiKey == null) ? -1 : kakaoRestApiKey.length();
        int trimmedLen = (appKey == null) ? -1 : appKey.length();
        log.info("[KakaoLocalClient] restApiKey rawLen={}, trimmedLen={}", rawLen, trimmedLen);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + appKey); // 공백 포함 정확히

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoCoord2RegionResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, KakaoCoord2RegionResponse.class);

            KakaoCoord2RegionResponse body = response.getBody();
            if (body == null || body.documents() == null || body.documents().isEmpty()) return null;

            // "H"(행정동) 우선 선택
            return body.documents().stream()
                    .sorted(Comparator.comparing(
                            (KakaoCoord2RegionResponse.Document d) -> !"H".equals(d.region_type())))
                    .findFirst()
                    .orElse(null);

        } catch (HttpStatusCodeException e) {
            log.warn("[KakaoLocalClient] FAILED status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }
}
