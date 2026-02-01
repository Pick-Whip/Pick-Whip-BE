package com.example.picknwhip_be.domain.review.service.query;

import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.review.converter.ReviewConverter;
import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {
  private final ReviewRepository reviewRepository;
  private final S3Service s3Service;

  @Override
  public ReviewResDTO.MyReviewListDTO getMyReviewList(Long cursor, int size, Long userId) {
    ReviewRow.MyReviewSummaryRow summary = reviewRepository.fetchMyReviewSummary(userId);
    long count = (summary == null) ? 0L : summary.count();
    double avgRating = roundTo1Decimal(summary == null ? null : summary.averageRating());

    int limit = size + 1;
    List<ReviewRow.MyReviewRow> rows = reviewRepository.fetchMyReviews(userId, cursor, limit);

    boolean hasNext = rows.size() > size;
    List<ReviewRow.MyReviewRow> pageRows = hasNext ? rows.subList(0, size) : rows;

    Long nextCursor = null;
    if (hasNext && !pageRows.isEmpty()) {
      nextCursor = pageRows.get(pageRows.size() - 1).reviewId();
    }

    List<Long> reviewIds = pageRows.stream().map(ReviewRow.MyReviewRow::reviewId).toList();

    List<ReviewRow.MyReviewImageRow> imageRows = reviewRepository.fetchMyReviewImages(reviewIds);
    Map<Long, List<String>> imageUrlsByReviewId = groupImageUrlsByReviewId(imageRows);

    List<ReviewRow.MyReviewReplyRow> replyRows = reviewRepository.fetchMyReviewReplies(reviewIds);
    Map<Long, String> replyByReviewId = groupReplyByReviewId(replyRows);

    return ReviewConverter.toMyReviewListDTO(
        count, avgRating, pageRows, imageUrlsByReviewId, replyByReviewId, nextCursor, hasNext);
  }

  private Map<Long, String> groupReplyByReviewId(List<ReviewRow.MyReviewReplyRow> replyRows) {
    Map<Long, String> map = new LinkedHashMap<>();
    for (ReviewRow.MyReviewReplyRow row : replyRows) {
      map.putIfAbsent(row.reviewId(), row.replyContent());
    }
    return map;
  }

  private Map<Long, List<String>> groupImageUrlsByReviewId(
      List<ReviewRow.MyReviewImageRow> imageRows) {
    Map<Long, List<String>> map = new LinkedHashMap<>();
    for (ReviewRow.MyReviewImageRow row : imageRows) {
      map.computeIfAbsent(row.reviewId(), k -> new ArrayList<>());
      List<String> urls = map.get(row.reviewId());
      if (urls.size() >= 5) {
        continue;
      }
      String url = s3Service.createPresignedDownloadUrl(row.s3Key());
      urls.add(url);
    }
    return map;
  }

  private double roundTo1Decimal(Double avg) {
    double value = (avg == null) ? 0.0 : avg;
    return Math.round(value * 10.0) / 10.0;
  }
}
