package com.example.picknwhip_be.domain.user.controller;

import com.example.picknwhip_be.domain.user.converter.UserConverter;
import com.example.picknwhip_be.domain.user.dto.req.UserRequestDTO;
import com.example.picknwhip_be.domain.user.dto.res.UserResponseDTO;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.service.UserCommandService;
import com.example.picknwhip_be.domain.user.service.UserQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  @Operation(summary = "사용자 정보 조회 API", description = "사용자 ID를 통해 프로필 정보를 조회합니다.")
  @GetMapping("/{userId}")
  public ApiResponse<UserResponseDTO.UserProfileDTO> getMemberProfile(
      @PathVariable @Parameter(description = "사용자 ID") Long userId) {
    User user = userQueryService.getUser(userId);
    return ApiResponse.of(GeneralSuccessCode.OK, UserConverter.toUserProfileDTO(user));
  }

  @Operation(summary = "사용자 정보 수정 API", description = "사용자의 닉네임, 전화번호, 프로필 이미지를 수정합니다.")
  @PatchMapping("/{userId}")
  public ApiResponse<UserResponseDTO.UpdateProfileResultDTO> updateMemberProfile(
      @PathVariable @Parameter(description = "사용자 ID") Long userId,
      @RequestBody UserRequestDTO.UpdateProfileDTO request) {
    User user = userCommandService.updateProfile(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, UserConverter.toUpdateProfileResultDTO(user));
  }
}
