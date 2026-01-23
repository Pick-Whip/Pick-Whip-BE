package com.example.picknwhip_be.domain.S3.controller;

import com.example.picknwhip_be.domain.S3.dto.req.S3ReqDTO;
import com.example.picknwhip_be.domain.S3.dto.res.S3ResDTO;
import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.global.apiPayload.ApiResponse;
import com.example.picknwhip_be.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "S3", description = "이미지 업로드 API")
@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {
  private final S3Service s3Service;

  @Operation(
      summary = "단일 presigned URL 생성 by 슝/하승연",
      description = "단일 이미지 업로드용(프로필 사진) presigned URL을 생성하는 기능입니다.")
  @PostMapping("/upload")
  public ApiResponse<S3ResDTO.PresignResponseDTO> createSingleUploadUrl(
      @RequestBody @Valid S3ReqDTO.SingleDTO request) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, s3Service.createProfileUploadUrl(request.fileName()));
  }

  @Operation(
      summary = "다중 presigned URL 생성 by 슝/하승연",
      description = "다중 이미지 업로드용(리뷰 사진) presigned URL을 생성하는 기능입니다.")
  @PostMapping("/upload/list")
  public ApiResponse<List<S3ResDTO.PresignResponseDTO>> createUploadUrlList(
      @RequestBody @Valid S3ReqDTO.BatchDTO request) {
    return ApiResponse.of(
        GeneralSuccessCode.OK, s3Service.createReviewUploadUrls(request.fileNames()));
  }
}
