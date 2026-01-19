package com.example.picknwhip_be.domain.S3.service;

import com.example.picknwhip_be.domain.S3.dto.res.S3ResDTO;
import com.example.picknwhip_be.domain.S3.exception.S3CustomException;
import com.example.picknwhip_be.domain.S3.exception.code.S3ErrorCode;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

  private static final Duration UPLOAD_EXPIRES = Duration.ofMinutes(10);
  private static final Duration DOWNLOAD_EXPIRES = Duration.ofMinutes(10);

  private final S3Presigner s3Presigner;

  @Value("${s3.bucket}")
  private String bucket;

  // 단일 이미지 업로드용(프로필 사진) presigned URL을 발급
  public S3ResDTO.PresignResponseDTO createProfileUploadUrl(String fileName) {
    validateBucketConfigured();
    validateFileName(fileName);

    String keyName = buildKey("profile", fileName);
    String url = presignPutUrl(keyName);

    return new S3ResDTO.PresignResponseDTO(keyName, url);
  }

  // 다중 이미지 업로드용(리뷰 사진) presigned URL을 발급
  public List<S3ResDTO.PresignResponseDTO> createReviewUploadUrls(List<String> fileNames) {
    validateBucketConfigured();

    if (fileNames == null) {
      throw new S3CustomException(S3ErrorCode.INVALID_FILE_NAME);
    }
    if (fileNames.size() > 5) {
      throw new S3CustomException(S3ErrorCode.TOO_MANY_FILES);
    }

    return fileNames.stream()
        .map(
            fileName -> {
              validateFileName(fileName);
              String keyName = buildKey("review", fileName);
              String url = presignPutUrl(keyName);
              return new S3ResDTO.PresignResponseDTO(keyName, url);
            })
        .toList();
  }

  // keyName을 받아서 조회 가능한 Presigned URL을 발급
  public String createPresignedDownloadUrl(String keyName) {
    validateBucketConfigured();
    validateKeyName(keyName);

    try {
      GetObjectRequest getObjectRequest =
          GetObjectRequest.builder().bucket(bucket).key(keyName).build();

      PresignedGetObjectRequest presigned =
          s3Presigner.presignGetObject(
              r -> r.signatureDuration(DOWNLOAD_EXPIRES).getObjectRequest(getObjectRequest));

      return presigned.url().toString();

    } catch (S3Exception e) {
      throw new S3CustomException(S3ErrorCode.S3_OPERATION_FAILED);
    } catch (SdkClientException e) {
      throw new S3CustomException(S3ErrorCode.AWS_SDK_CLIENT_ERROR);
    } catch (Exception e) {
      throw new S3CustomException(S3ErrorCode.PRESIGNED_URL_GENERATION_FAILED);
    }
  }

  // keyName으로 presigned URL을 발급하는 로직
  private String presignPutUrl(String keyName) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucket).key(keyName).build();

    try {
      PresignedPutObjectRequest presigned =
          s3Presigner.presignPutObject(
              r -> r.signatureDuration(UPLOAD_EXPIRES).putObjectRequest(putObjectRequest));

      return presigned.url().toString();

    } catch (S3Exception e) {
      throw new S3CustomException(S3ErrorCode.S3_OPERATION_FAILED);

    } catch (SdkClientException e) {
      throw new S3CustomException(S3ErrorCode.AWS_SDK_CLIENT_ERROR);

    } catch (Exception e) {
      throw new S3CustomException(S3ErrorCode.PRESIGNED_URL_GENERATION_FAILED);
    }
  }

  private void validateBucketConfigured() {
    if (!StringUtils.hasText(bucket)) {
      throw new S3CustomException(S3ErrorCode.S3_BUCKET_NOT_CONFIGURED);
    }
  }

  private void validateFileName(String fileName) {
    if (!StringUtils.hasText(fileName)) {
      throw new S3CustomException(S3ErrorCode.INVALID_FILE_NAME);
    }
  }

  private void validateKeyName(String keyName) {
    if (!StringUtils.hasText(keyName)) {
      throw new S3CustomException(S3ErrorCode.INVALID_FILE_NAME);
    }
  }

  // S3 keyName 만들기
  private String buildKey(String folder, String originalFilename) {
    String safeName = sanitizeFilename(originalFilename);
    String uuid = UUID.randomUUID().toString();
    return folder + "/" + uuid + "/" + safeName;
  }

  // 위험한 파일명 정리
  private String sanitizeFilename(String originalFilename) {
    String filename = StringUtils.cleanPath(originalFilename);
    filename = filename.replace("\\", "/");

    while (filename.contains("..")) {
      filename = filename.replace("..", "");
    }

    int lastSlash = filename.lastIndexOf('/');
    if (lastSlash >= 0) {
      filename = filename.substring(lastSlash + 1);
    }

    if (!StringUtils.hasText(filename)) {
      throw new S3CustomException(S3ErrorCode.INVALID_FILE_NAME);
    }
    return filename;
  }
}
