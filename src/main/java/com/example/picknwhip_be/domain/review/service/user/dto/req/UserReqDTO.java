package com.example.picknwhip_be.domain.review.service.user.dto.req;

import com.example.picknwhip_be.domain.review.service.user.entity.WithdrawalReason;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserReqDTO {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateProfileDTO {
    private String nickname;
    private String profileImageUrl;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ExtraInfoDTO {
    private String name;
    private String phone;

    @Pattern(regexp = "^\\d{7}$", message = "생년월일은 숫자 7자리(YYMMDDG) 형식이어야 합니다.")
    private String birthdate;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WithdrawalDTO {
    private List<WithdrawalReason> reasons;
    private String feedback;
  }
}
