package com.example.picknwhip_be.domain.review.service.query.cursor;

import com.example.picknwhip_be.domain.review.enums.ReviewSort;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class ReviewCursorCodec {

  public String encode(ReviewSort sort, ReviewCursor cursor) {
    if (cursor == null) return null;

    String raw =
        switch (sort) {
          case LATEST -> cursor.createdAt() + "|" + cursor.reviewId();
          case HELPFUL, RATING_HIGH, RATING_LOW -> cursor.primaryValue() + "|" + cursor.reviewId();
        };

    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
  }

  public ReviewCursor decode(ReviewSort sort, String encoded) {
    if (encoded == null || encoded.isBlank()) return null;

    String raw = new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
    String[] parts = raw.split("\\|");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid cursor format");
    }

    return switch (sort) {
      case LATEST ->
          ReviewCursor.forLatest(LocalDateTime.parse(parts[0]), Long.parseLong(parts[1]));
      case HELPFUL, RATING_HIGH, RATING_LOW ->
          ReviewCursor.forPrimary(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
    };
  }
}
