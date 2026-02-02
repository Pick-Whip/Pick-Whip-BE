package com.example.picknwhip_be.domain.shop.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;
import lombok.*;

public class PickupResDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PickupCalendarDTO {
        private String date;
        private boolean isClosed;
        private List<TimeSlotDTO> slots;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotDTO {
        @JsonFormat(pattern = "HH:mm")
        private LocalTime time;
        private boolean isAvailable;
    }
}