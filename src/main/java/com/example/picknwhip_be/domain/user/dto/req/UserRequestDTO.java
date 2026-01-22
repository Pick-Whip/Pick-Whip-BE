package com.example.picknwhip_be.domain.user.dto.req;

import com.example.picknwhip_be.domain.user.entity.WithdrawalReason;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDTO {

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
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WithdrawalDTO {
    private List<WithdrawalReason> reasons;
    private String feedback;
  }
}
