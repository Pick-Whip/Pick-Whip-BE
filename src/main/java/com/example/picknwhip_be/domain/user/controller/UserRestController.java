package com.example.picknwhip_be.domain.user.controller;

import com.example.picknwhip_be.domain.user.converter.UserConverter;
import com.example.picknwhip_be.domain.user.dto.req.UserReqDTO;
import com.example.picknwhip_be.domain.user.dto.res.UserResDTO;
import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.service.command.UserCommandService;
import com.example.picknwhip_be.domain.user.service.query.UserQueryService;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.annotation.ExtractPayload;
import com.example.picknwhip_be.global.apiPayload.code.AuthErrorCode;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import com.example.picknwhip_be.global.apiPayload.util.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  @Value("${jwt.refresh-token-validity:1209600000}")
  private long refreshTokenValidityInMilliseconds;

  @Operation(
      summary = "리프레시토큰 발급 API",
      description = "액세스 토큰 만료 시 호출합니다. 쿠키 또는 body의 refreshToken을 사용합니다.")
  @PostMapping("/refresh")
  public ApiResponse<UserResDTO.TokenResponseDTO> refresh(
      @RequestBody(required = false) UserReqDTO.RefreshTokenReqDTO dto,
      HttpServletRequest request,
      HttpServletResponse response) {
    String token =
        CookieUtils.getCookie(request, "refreshToken")
            .map(Cookie::getValue)
            .orElseGet(
                () ->
                    (dto != null && dto.getRefreshToken() != null) ? dto.getRefreshToken() : null);
    if (token == null || token.isBlank()) {
      throw new GeneralException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }
    UserResDTO.TokenResponseDTO result = userCommandService.refreshAccessToken(token);
    int cookieMaxAge = (int) (refreshTokenValidityInMilliseconds / 1000);

    CookieUtils.addRefreshTokenCookie(
        response, "refreshToken", result.getRefreshToken(), cookieMaxAge);

    return ApiResponse.of(GeneralSuccessCode.OK, result);
  }

  @Operation(summary = "신규 가입 유저 추가 정보 저장 API", description = "이름과 휴대폰 번호, 생일을 입력받아 업데이트합니다.")
  @PostMapping("/extra/info")
  public ApiResponse<String> createExtraInfo(
      @ExtractPayload Long userId, @Valid @RequestBody UserReqDTO.ExtraInfoDTO request) {
    userCommandService.updateExtraInfo(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, "정보 저장이 완료되었습니다.");
  }

  @Operation(summary = "내 정보 조회 API", description = "로그인된 사용자의 프로필 정보를 조회합니다.")
  @GetMapping("/me")
  public ApiResponse<UserResDTO.UserProfileDTO> getMemberProfile(@ExtractPayload Long userId) {
    User user = userQueryService.getUser(userId);
    return ApiResponse.of(GeneralSuccessCode.OK, UserConverter.toUserProfileDTO(user));
  }

  @Operation(summary = "내 정보 수정 API", description = "로그인된 사용자의 정보를 수정합니다.")
  @PatchMapping("/me")
  public ApiResponse<UserResDTO.UpdateProfileResultDTO> updateMemberProfile(
      @ExtractPayload Long userId, @RequestBody UserReqDTO.UpdateProfileDTO request) {
    User user = userCommandService.updateProfile(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, UserConverter.toUpdateProfileResultDTO(user));
  }

  @Operation(summary = "회원 탈퇴 API", description = "로그인된 사용자를 탈퇴 처리합니다.")
  @PostMapping("/withdraw")
  public ApiResponse<String> withdrawMember(
      @ExtractPayload Long userId, @RequestBody UserReqDTO.WithdrawalDTO request) {
    userCommandService.withdrawMember(userId, request);
    return ApiResponse.of(GeneralSuccessCode.OK, "탈퇴가 정상적으로 처리되었습니다.");
  }

  @Operation(summary = "로그아웃 API", description = "서버 세션을 만료시키고 카카오 로그아웃을 수행합니다.")
  @PostMapping("/logout")
  public ApiResponse<String> createLogout(
      @ExtractPayload Long userId, HttpServletRequest request, HttpServletResponse response) {
    try {
      userCommandService.logout(userId);
    } finally {
      CookieUtils.deleteCookie(request, response, "refreshToken");
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
    }
    return ApiResponse.of(GeneralSuccessCode.OK, "로그아웃 되었습니다.");
  }
}
