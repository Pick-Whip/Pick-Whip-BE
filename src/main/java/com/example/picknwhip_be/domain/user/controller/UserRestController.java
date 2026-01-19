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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  @Operation(summary = "신규 가입 유저 추가 정보 저장 API", description = "이름과 휴대폰 번호를 입력받아 업데이트합니다.")
  @PostMapping("/extra/info")
  public ApiResponse<String> createExtraInfo(
      @Valid @RequestBody UserRequestDTO.ExtraInfoDTO request) {
    // TODO: JWT 완성 후 SecurityContext에서 추출하도록 변경 필요
    Long userId = 1L;
    userCommandService.updateExtraInfo(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, "정보 저장이 완료되었습니다.");
  }

  @Operation(summary = "내 정보 조회 API", description = "로그인된 사용자의 프로필 정보를 조회합니다.")
  @GetMapping("/me")
  public ApiResponse<UserResponseDTO.UserProfileDTO> getMemberProfile() {
    // TODO: 실제 로그인 구현 후 SecurityContext에서 ID를 가져와야 함. 현재는 임시로 1L 사용.
    Long userId = 1L;
    User user = userQueryService.getUser(userId);
    return ApiResponse.of(GeneralSuccessCode.OK, UserConverter.toUserProfileDTO(user));
  }

  @Operation(summary = "내 정보 수정 API", description = "로그인된 사용자의 정보를 수정합니다.")
  @PatchMapping("/me")
  public ApiResponse<UserResponseDTO.UpdateProfileResultDTO> updateMemberProfile(
      @RequestBody UserRequestDTO.UpdateProfileDTO request) {
    // TODO: SecurityContext 연동 필요
    Long userId = 1L;
    User user = userCommandService.updateProfile(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, UserConverter.toUpdateProfileResultDTO(user));
  }

  @Operation(summary = "회원 탈퇴 API", description = "로그인된 사용자를 탈퇴 처리합니다.")
  @PostMapping("/withdraw")
  public ApiResponse<String> withdrawMember(@RequestBody UserRequestDTO.WithdrawalDTO request) {
    // TODO: SecurityContext 연동 필요
    Long userId = 1L;
    userCommandService.withdrawMember(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, "탈퇴가 정상적으로 처리되었습니다.");
  }

  @Operation(summary = "로그아웃 API", description = "서버 세션을 만료시키고 카카오 로그아웃을 수행합니다.")
  @PostMapping("/logout")
  public ApiResponse<String> createLogout(HttpServletRequest request) {
    // TODO: SecurityContext 연동 필요 (현재는 임시 1L 사용)
    Long userId = 1L;
    try {
      // 카카오 서버 로그아웃
      userCommandService.logout(userId);
    } finally {
      // 우리 서버 세션 무효화
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
    }
    return ApiResponse.of(GeneralSuccessCode.OK, "로그아웃 되었습니다.");
  }
}
