package com.example.picknwhip_be.domain.review.scheduler;

import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.review.repository.ReviewImageRepository;
import com.example.picknwhip_be.domain.review.repository.ReviewRepository;
import com.example.picknwhip_be.domain.review.service.command.ReviewPurgeService;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewPurgeScheduler {
  private static final long RETENTION_DAYS = 30;

  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;
  private final ReviewPurgeService reviewPurgeService;
  private final S3Service s3Service;
  private final Clock clock;

  // 매일 4:00AM에 실행
  @Scheduled(cron = "0 0 4 * * *")
  public void purgeDeletedReviews() {

    LocalDateTime cutoff = LocalDateTime.now(clock).minusDays(RETENTION_DAYS);
    List<Long> targetIds = reviewRepository.findPurgeTargetIds(cutoff);
      log.warn("[ReviewPurge] start cutoff={}", cutoff);
      log.warn("[ReviewPurge] targetIds={}", targetIds);

    if (targetIds.isEmpty()) {
      return;
    }

    int successCount = 0;

    for (Long reviewId : targetIds) {
      try {
        List<String> s3Keys = reviewImageRepository.findS3KeysByReviewId(reviewId);
        s3Service.deleteObjects(s3Keys);

        int deleted = reviewPurgeService.hardDeleteReviewGraph(reviewId);
        successCount += deleted;
      } catch (Exception e) {
        log.error("purge failed. reviewId={}", reviewId, e);
      }
    }

    log.info(
        "purgeDeletedReviews done. cutoff={}, targetCount={}, successCount={}",
        cutoff,
        targetIds.size(),
        successCount);
  }
}
