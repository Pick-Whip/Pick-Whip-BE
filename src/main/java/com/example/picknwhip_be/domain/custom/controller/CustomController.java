package com.example.picknwhip_be.domain.custom.controller;

import com.example.picknwhip_be.domain.custom.dto.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.service.CustomService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customs")
public class CustomController {

  private final CustomService customService;

  @PostMapping("")
  public ApiResponse<CustomResDTO.CustomCreateDTO> postCustom(
      @RequestBody CustomReqDTO.CustomCreateDTO dto) {
    return ApiResponse.of(GeneralSuccessCode.CREATED, customService.saveCustom(dto));
  }
}
