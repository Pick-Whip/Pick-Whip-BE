package com.example.picknwhip_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WithdrawalReasonItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "withdrawal_id")
  private UserWithdrawal userWithdrawal;

  @Enumerated(EnumType.STRING)
  private WithdrawalReason reasonType;
}
