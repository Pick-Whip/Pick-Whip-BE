package com.example.picknwhip_be.global.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
