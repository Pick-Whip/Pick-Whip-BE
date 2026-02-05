package com.example.picknwhip_be.domain.shop.dto.res;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.*;

public class PickupResDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStatusDTO {
        private LocalDate date;
        private boolean isClosed;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailySlotsDTO {
        private String date;
        private String formattedDate;
        private boolean isClosed;
        private List<TimeSlotDTO> slots;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotDTO {
        private LocalTime time;
        private String timeLabel;
        private boolean isAvailable;
        private String reason;
    }
}
