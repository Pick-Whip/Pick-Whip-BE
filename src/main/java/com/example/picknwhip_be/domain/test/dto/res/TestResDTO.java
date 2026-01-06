package com.example.picknwhip_be.domain.test.dto.res;

import lombok.Builder;
import lombok.Getter;

public class TestResDTO {

  @Builder
  @Getter
  public static class Exception {   private String testString;
  }
}
