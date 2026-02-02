package com.example.picknwhip_be.domain.shop.entity.mapping;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  private String addressName;
  private String roadAddressName;

  private String region1DepthName;
  private String region2DepthName;
  private String region3DepthName;
}
