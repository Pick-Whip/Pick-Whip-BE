package com.example.picknwhip_be.domain.test.controller;

import com.example.picknwhip_be.domain.test.converter.TestConverter;
import com.example.picknwhip_be.domain.test.dto.res.TestResDTO;
import com.example.picknwhip_be.domain.test.service.query.TestQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempRestController {

  private final TestQueryService testQueryService;

  @GetMapping("/exception")
  public ApiResponse<TestResDTO.Exception> exception(@RequestParam Long flag) {

    testQueryService.checkFlag(flag);

    GeneralSuccessCode code = GeneralSuccessCode.OK;
    return ApiResponse.of(code, TestConverter.toExceptionDTO("This is Test!"));
  }
}
