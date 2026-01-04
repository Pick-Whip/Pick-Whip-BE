package com.example.picknwhip_be.domain.test.converter;

import com.example.picknwhip_be.domain.test.dto.res.TestResDTO;

public class TestConverter {

  public static TestResDTO.Exception toExceptionDTO(String testing) {
    return TestResDTO.Exception.builder().testString(testing).build();
  }
}
