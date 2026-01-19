package com.example.picknwhip_be.domain.custom.dto;

import java.time.LocalDateTime;
import lombok.Builder;

public class CustomResDTO {

  @Builder
  public record CustomCreateDTO(Long customId, LocalDateTime createAt) {}
}
