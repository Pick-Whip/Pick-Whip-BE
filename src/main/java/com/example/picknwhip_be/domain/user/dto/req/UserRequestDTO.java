package com.example.picknwhip_be.domain.user.dto.req;

import com.example.picknwhip_be.domain.user.entity.WithdrawalReason;
import java.util.List;
import lombok.Getter;

public class UserRequestDTO {

  @Getter
  public static class UpdateProfileDTO {
    private String nickname;
    private String phone;
    private String profileImageUrl;
  }

  @Getter
  public static class WithdrawalDTO {
    private List<WithdrawalReason> reasons;
    private String feedback;
  }
}
