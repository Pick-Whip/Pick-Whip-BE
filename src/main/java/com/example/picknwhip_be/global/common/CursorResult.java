package com.example.picknwhip_be.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CursorResult<T> {
    private List<T> content;
    private boolean hasNext;
    private Cursor nextCursor;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cursor {
        private Integer statusScore;
        private String pickupDatetime;
        private Long orderId;
    }
}