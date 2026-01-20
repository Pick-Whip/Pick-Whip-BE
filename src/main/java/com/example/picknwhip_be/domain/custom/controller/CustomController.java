package com.example.picknwhip_be.domain.custom.controller;

import com.example.picknwhip_be.domain.custom.dto.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import com.example.picknwhip_be.domain.custom.service.CustomCommandService;
import com.example.picknwhip_be.domain.custom.service.CustomQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customs")
public class CustomController {

  private final CustomQueryService customQueryService;
  private final CustomCommandService customCommandService;

  @PostMapping("")
  public ApiResponse<CustomResDTO.CustomCreateDTO> postCustom(
      @Valid @RequestBody CustomReqDTO.CustomCreateDTO dto) {
    return ApiResponse.of(GeneralSuccessCode.CREATED, customCommandService.saveCustom(dto));
  }

  @GetMapping("/drafts")
  public ApiResponse<List<CustomResDTO.GetDraftListDTO>> getDraftList() {

    Long userId = 1L; // 추후 @AuthenticationPrincipal로 교체
    List<CustomResDTO.GetDraftListDTO> result = customQueryService.findDraftList(userId);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }
}
