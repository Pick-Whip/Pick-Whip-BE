package com.example.picknwhip_be.domain.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LetteringLineCount {
  ONE_LINE("1줄"),
  TWO_LINE("2줄"),
  THREE_LINE("3줄");

  private final String description;
}
