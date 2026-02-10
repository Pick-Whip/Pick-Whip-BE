package com.example.picknwhip_be.domain.design.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Style {

  // 디자인 스타일
  MINIMAL("미니멀"),
  GORGEOUS("화려한"),
  VINTAGE("빈티지"),
  MODERN("모던"),
  LOVELY("러블리"),
  ANTIQUE("앤틱"),
  SIMPLE("심플"),
  LUXURY("럭셔리"),

  // 케이크 형태
  ROUND("원형"),
  HEART("하트"),
  SQUARE("사각"),

  // 맛 / 베이스
  VANILLA("바닐라"),
  CHOCOLATE("초콜릿"),
  STRAWBERRY("딸기"),
  MATCHA("말차"),
  CHEESE("치즈"),
  TIRAMISU("티라미수"),
  RED_VELVET("레드벨벳"),
  CARROT("당근"),
  EARL_GREY("얼그레이"),

  // 토핑 / 데코레이션
  FRESH_FRUIT("생과일"),
  MACARON("마카롱"),
  FLOWER("생화"),
  FIGURINE("조화"),
  GOLD_LEAF("금박"),
  CHOCOLATE_TOPPING("초콜릿"),
  COOKIE("쿠키"),
  MERINGUE("머랭"),

  // 특별 옵션
  PHOTO_CAKE("포토케이크"),
  LETTERING("레터링"),
  IDOL("아이돌"),
  GLUTEN_FREE("글루텐프리"),
  VEGAN("비건"),

  // 용도
  BIRTHDAY("생일"),
    ANNIVERSARY("기념일"),
    CHRISTMAS("크리스마스"),
    GRADUATION("졸업"),
    OPENING("개업");

  private final String label;
}
