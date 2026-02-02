package com.example.picknwhip_be.domain.review.service.query;

import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.repository.OrderItemRepository;
import com.example.picknwhip_be.domain.review.converter.ReviewConverter;
import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewSelectedKeyword;
import com.example.picknwhip_be.domain.review.repository.ReviewRepository;
import com.example.picknwhip_be.domain.review.repository.ReviewSelectedKeywordRepository;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import java.time.format.DateTimeFormatter;
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
  private final OrderItemRepository orderItemRepository;
  private final ReviewSelectedKeywordRepository reviewSelectedKeywordRepository;

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

  @Override
  public ReviewResDTO.BestReviewListDTO getBestCustomReviews() {
    List<Review> reviews = reviewRepository.findBestHelpfulReviews(5);

    if (reviews.isEmpty()) {
      return ReviewResDTO.BestReviewListDTO.builder().items(List.of()).build();
    }

    List<Long> orderIds = reviews.stream().map(r -> r.getOrder().getId()).toList();
    List<OrderItem> allOrderItems = orderItemRepository.findAllByOrderIdIn(orderIds);
    List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
    List<ReviewSelectedKeyword> allKeywords =
        reviewSelectedKeywordRepository.findAllByReviewIdIn(reviewIds);

    List<ReviewResDTO.BestReviewItemDTO> items =
        reviews.stream()
            .map(
                review -> {
                  List<OrderItem> myOptions =
                      allOrderItems.stream()
                          .filter(item -> item.getOrder().getId().equals(review.getOrder().getId()))
                          .toList();

                  List<String> myKeywords =
                      allKeywords.stream()
                          .filter(k -> k.getReview().getId().equals(review.getId()))
                          .map(k -> k.getKeyword().getLabel())
                          .toList();

                  return convertToBestReviewItemDTO(review, myOptions, myKeywords);
                })
            .toList();

    return ReviewResDTO.BestReviewListDTO.builder().items(items).build();
  }

  private ReviewResDTO.BestReviewItemDTO convertToBestReviewItemDTO(
      Review review, List<OrderItem> options, List<String> keywords) {

    Order order = review.getOrder();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    String sheetName = "";
    String creamName = "";
    List<String> decos = new ArrayList<>();

    String icingColor = null;
    String sheetColor = null;
    String creamColor = null;

    for (OrderItem item : options) {
      if (item.getOptionCategory() == null) continue;

      OptionCategory category = item.getOptionCategory();
      switch (category.name()) {
        case "SHEET":
          sheetName = item.getOptionName();
          if (item.getColorRgbCode() != null) sheetColor = item.getColorRgbCode();
          break;
        case "CREAM":
          creamName = item.getOptionName();
          if (item.getColorRgbCode() != null) creamColor = item.getColorRgbCode();
          break;
        case "TOPPING": // or DECO
          decos.add(item.getOptionName());
          break;
        case "ICING": // or COLOR
          if (item.getColorRgbCode() != null) icingColor = item.getColorRgbCode();
          break;
        case "SHEET_COLOR":
          sheetColor = item.getColorRgbCode();
          break;
        case "CREAM_COLOR":
          creamColor = item.getColorRgbCode();
          break;
      }
    }

    String taste = sheetName + (creamName.isEmpty() ? "" : " + " + creamName);
    String deco = String.join(", ", decos);
    ReviewResDTO.CakeColorsDTO colorsDTO =
        ReviewResDTO.CakeColorsDTO.builder()
            .icingColor(icingColor)
            .sheetColor(sheetColor)
            .creamColor(creamColor)
            .build();

    ReviewResDTO.CakeOptionDTO optionDTO =
        ReviewResDTO.CakeOptionDTO.builder()
            .designName(
                order.getDesignGallery() != null
                    ? order.getDesignGallery().getDesignName()
                    : "Unknown Design")
            .taste(taste)
            .deco(deco)
            .additionalRequest(order.getAdditionalRequest())
            .colors(colorsDTO)
            .build();
    String resultImageUrl = order.getReferenceImageUrl();

    return ReviewResDTO.BestReviewItemDTO.builder()
        .writerName(review.getUser().getNickname())
        .createdDate(review.getCreatedAt().format(formatter))
        .rating(review.getRating())
        .helpfulCount(review.getHelpfulCount())
        .content(review.getContent())
        .keywords(keywords)
        .cakeImageUrl(resultImageUrl)
        .options(optionDTO)
        .build();
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
