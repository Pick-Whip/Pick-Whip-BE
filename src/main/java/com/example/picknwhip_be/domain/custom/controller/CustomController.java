package com.example.picknwhip_be.domain.custom.controller;

import com.example.picknwhip_be.domain.custom.dto.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.service.CustomCommandService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customs")
public class CustomController {

  private final CustomCommandService customService;

  @PostMapping("")
  public ApiResponse<CustomResDTO.CustomCreateDTO> postCustom(
      @Valid @RequestBody CustomReqDTO.CustomCreateDTO dto) {
    return ApiResponse.of(GeneralSuccessCode.CREATED, customService.saveCustom(dto));
  }

  @GetMapping("/drafts")
  public ApiResponse<CustomResDTO.GetDraftListDTO> getDraftList() {
    List<CustomResDTO.GetDraftListDTO> draftListDTOS = new ArrayList<>();
    return null;
  }
}
