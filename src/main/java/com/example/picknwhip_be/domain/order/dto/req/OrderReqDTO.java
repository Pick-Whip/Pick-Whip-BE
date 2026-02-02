package com.example.picknwhip_be.domain.order.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class OrderReqDTO {

    @Getter
    public static class CreateOrderDTO {
        @NotNull
        private Long draftId;

        @NotBlank
        private String depositorName;

        @NotBlank
        private String phoneNumber;
    }
}