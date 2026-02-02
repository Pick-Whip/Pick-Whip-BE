package com.example.picknwhip_be.domain.shop.entity.mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  @Column(name = "address_name")
  private String addressName;

  @Column(name = "road_address_name")
  private String roadAddressName;

  @Column(name = "region1_depth_name")
  private String region1DepthName;

  @Column(name = "region2_depth_name")
  private String region2DepthName;

  @Column(name = "region3_depth_name")
  private String region3DepthName;
}
