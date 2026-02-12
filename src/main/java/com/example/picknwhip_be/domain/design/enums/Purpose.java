package com.example.picknwhip_be.domain.design.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Purpose {
  XMAS("크리스마스"),
  ANNIVERSARY("기념일"),
  BIRTHDAY("생일"),
  GRADUATION("졸업"),
  OPENING("개업");

  private final String label;
}
