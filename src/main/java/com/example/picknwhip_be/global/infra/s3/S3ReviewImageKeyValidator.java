package com.example.picknwhip_be.global.infra.s3;

import com.example.picknwhip_be.domain.review.exception.ReviewException;
import com.example.picknwhip_be.domain.review.exception.code.ReviewErrorCode;
import com.example.picknwhip_be.domain.review.validator.ReviewImageKeyValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
public class S3ReviewImageKeyValidator implements ReviewImageKeyValidator {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Override
  public void validateAll(List<String> imageKeys) {
    validateBucketConfigured();

    for (String key : imageKeys) {
      validateKeyFormat(key);
      validateObjectExists(key);
    }
  }

  private void validateBucketConfigured() {
    if (!StringUtils.hasText(bucket)) {
      throw new ReviewException(ReviewErrorCode.REVIEW_IMAGE_VALIDATION_FAILED);
    }
  }

  private void validateKeyFormat(String key) {
    if (!StringUtils.hasText(key)) {
      throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_IMAGE_KEY);
    }

    if (!key.startsWith("review/")) {
      throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_IMAGE_KEY);
    }
  }

  private void validateObjectExists(String key) {
    try {
      s3Client.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());
    } catch (NoSuchKeyException e) {
      throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_IMAGE_KEY);
    } catch (S3Exception e) {
      throw new ReviewException(ReviewErrorCode.REVIEW_IMAGE_VALIDATION_FAILED);
    }
  }
}
