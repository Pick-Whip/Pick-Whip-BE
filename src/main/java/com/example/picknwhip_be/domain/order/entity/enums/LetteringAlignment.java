package com.example.picknwhip_be.domain.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LetteringAlignment {
  CENTER("가운데"),
  CURVE_UP("위로 둥글게"),
  CURVE_UP_DOWN("위아래 둥글게");

  private final String description;
}
