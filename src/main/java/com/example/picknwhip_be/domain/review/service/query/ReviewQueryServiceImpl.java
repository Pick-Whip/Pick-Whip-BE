package com.example.picknwhip_be.domain.review.service.query;

import com.example.picknwhip_be.domain.S3.service.S3Service;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.OrderItem;
import com.example.picknwhip_be.domain.order.repository.OrderItemRepository;
import com.example.picknwhip_be.domain.review.converter.ReviewConverter;
import com.example.picknwhip_be.domain.review.dto.ReviewRow;
import com.example.picknwhip_be.domain.review.dto.req.ReviewReqDTO;
import com.example.picknwhip_be.domain.review.dto.res.ReviewResDTO;
import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewSelectedKeyword;
import com.example.picknwhip_be.domain.review.enums.ReviewSort;
import com.example.picknwhip_be.domain.review.exception.ReviewException;
import com.example.picknwhip_be.domain.review.exception.code.ReviewErrorCode;
import com.example.picknwhip_be.domain.review.repository.ReviewLikeRepository;
import com.example.picknwhip_be.domain.review.repository.ReviewRepository;
import com.example.picknwhip_be.domain.review.repository.ReviewSelectedKeywordRepository;
import com.example.picknwhip_be.domain.review.service.query.cursor.ReviewCursor;
import com.example.picknwhip_be.domain.review.service.query.cursor.ReviewCursorCodec;
import com.example.picknwhip_be.domain.shop.entity.enums.OptionCategory;
import com.example.picknwhip_be.domain.shop.exception.ShopException;
import com.example.picknwhip_be.domain.shop.exception.code.ShopErrorCode;
import com.example.picknwhip_be.domain.shop.repository.ShopRepository;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {
  private final ReviewRepository reviewRepository;
  private final S3Service s3Service;
  private final OrderItemRepository orderItemRepository;
  private final ReviewSelectedKeywordRepository reviewSelectedKeywordRepository;
  private final ReviewLikeRepository reviewLikeRepository;
  private final ReviewCursorCodec cursorCodec;
  private final ShopRepository shopRepository;

  @Override
  public ReviewResDTO.MyReviewListDTO getMyReviewList(Long cursor, int size, Long userId) {
    ReviewRow.ReviewSummaryRow summary = reviewRepository.fetchMyReviewSummary(userId);
    long count = (summary == null) ? 0L : summary.count();
    double avgRating = roundTo1Decimal(summary == null ? null : summary.averageRating());

    int limit = size + 1;
    List<ReviewRow.MyReviewRow> rows = reviewRepository.fetchMyReviews(userId, cursor, limit);

    boolean hasNext = rows.size() > size;
    List<ReviewRow.MyReviewRow> pageRows = hasNext ? rows.subList(0, size) : rows;

    Long nextCursor = null;
    if (hasNext && !pageRows.isEmpty()) {
      nextCursor = pageRows.getLast().reviewId();
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
  public ReviewResDTO.ReviewDetailDTO getReviewDetail(Long reviewId) {
    ReviewRow.ReviewDetailRow review = reviewRepository.fetchReviewDetail(reviewId);
    if (review == null) {
      throw new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND);
    }
    List<ReviewRow.KeywordRow> keywordRows = reviewRepository.fetchReviewDetailKeywords(reviewId);
    List<ReviewRow.MyReviewImageRow> imageRows =
        reviewRepository.fetchMyReviewImages(List.of(reviewId));
    List<String> imageUrls = extractImageUrlsForReview(reviewId, imageRows);

    List<ReviewResDTO.KeywordDTO> keywords =
        keywordRows.stream().map(k -> new ReviewResDTO.KeywordDTO(k.code(), k.label())).toList();

    return ReviewConverter.toReviewDetailDTO(
        reviewId,
        review.rating(),
        review.content(),
        review.reply(),
        imageUrls,
        review.createdAt(),
        review.nickname(),
        review.profileUrl(),
        keywords);
  }

  @Override
  public ReviewResDTO.ShopReviewListDTO searchShopReviews(
      Long shopId, ReviewReqDTO.ShopReviewListDTO dto, Long userId) {
    if (!shopRepository.existsById(shopId)) {
      throw new ShopException(ShopErrorCode.SHOP_NOT_FOUND);
    }

    ReviewSort sort = dto.sort() != null ? dto.sort() : ReviewSort.LATEST;
    int size = dto.size() != null ? dto.size() : 20;

    ReviewCursor cursor;
    try {
      cursor = cursorCodec.decode(sort, dto.cursor());
    } catch (IllegalArgumentException | DateTimeParseException e) {
      throw new ReviewException(ReviewErrorCode.INVALID_CURSOR);
    }

    int limit = size + 1;

    List<ReviewRow.ShopReviewRow> rows =
        reviewRepository.fetchShopReviewRows(
            shopId, sort, dto.designIds(), dto.styles(), cursor, limit);

    boolean hasNext = rows.size() > size;
    List<ReviewRow.ShopReviewRow> page = hasNext ? rows.subList(0, size) : rows;

    String nextCursor = null;
    if (hasNext && !page.isEmpty()) {
      ReviewRow.ShopReviewRow last = page.getLast();
      nextCursor = cursorCodec.encode(sort, ReviewCursor.fromRow(sort, last));
    }

    List<Long> reviewIds = page.stream().map(ReviewRow.ShopReviewRow::reviewId).toList();

    // 배치 조회: 이미지, 키워드, 좋아요 여부
    List<ReviewRow.MyReviewImageRow> imageRows = reviewRepository.fetchMyReviewImages(reviewIds);
    Map<Long, List<String>> imageUrlsByReviewId = groupImageUrlsByReviewId(imageRows);

    List<ReviewRow.KeywordRow> keywordRows = reviewRepository.fetchReviewKeywords(reviewIds);
    Map<Long, List<ReviewResDTO.KeywordDTO>> keywordsByReviewId =
        keywordRows.stream()
            .collect(
                Collectors.groupingBy(
                    ReviewRow.KeywordRow::reviewId,
                    Collectors.mapping(
                        k -> new ReviewResDTO.KeywordDTO(k.code(), k.label()),
                        Collectors.toList())));

    Set<Long> likedReviewIds = reviewRepository.fetchLikedReviewIds(userId, reviewIds);

    return ReviewConverter.toShopReviewListDTO(
        page, imageUrlsByReviewId, keywordsByReviewId, likedReviewIds, nextCursor, hasNext);
  }

  @Override
  public ReviewResDTO.ShopReviewSummaryDTO searchShopReviewSummary(Long shopId) {
    if (!shopRepository.existsById(shopId)) {
      throw new ShopException(ShopErrorCode.SHOP_NOT_FOUND);
    }

    ReviewRow.ReviewSummaryRow summary = reviewRepository.fetchShopReviewSummary(shopId);
    double rating = roundTo1Decimal(summary == null ? null : summary.averageRating());
    int count = summary == null ? 0 : (int) summary.count();

    List<ReviewRow.KeywordCategoryCountRow> categoryCounts = Collections.emptyList();
    if (count > 0) {
      categoryCounts = reviewRepository.fetchShopKeywordCategoryCounts(shopId);
    }

    return ReviewConverter.toShopReviewSummaryDTO(rating, count, categoryCounts);
  }

  @Override
  public ReviewResDTO.BestReviewListDTO getBestCustomReviews() {
    List<Review> reviews = reviewRepository.findBestHelpfulReviews(5);

    if (reviews.isEmpty()) {
      return ReviewResDTO.BestReviewListDTO.builder().items(List.of()).build();
    }

    List<Long> orderIds = reviews.stream().map(r -> r.getOrder().getId()).toList();
    List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

    List<OrderItem> allOrderItems = orderItemRepository.findAllByOrderIdIn(orderIds);
    List<ReviewSelectedKeyword> allKeywords =
        reviewSelectedKeywordRepository.findAllByReviewIdIn(reviewIds);

    List<Object[]> likeCountData = reviewLikeRepository.countByReviewIdIn(reviewIds);
    Map<Long, Long> likeCounts =
        likeCountData.stream()
            .collect(
                Collectors.toMap(
                    row -> (Long) row[0], // reviewId
                    row -> (Long) row[1] // count
                    ));

    Map<Long, List<OrderItem>> optionsByOrderId =
        allOrderItems.stream().collect(Collectors.groupingBy(item -> item.getOrder().getId()));

    Map<Long, List<String>> keywordsByReviewId =
        allKeywords.stream()
            .collect(
                Collectors.groupingBy(
                    k -> k.getReview().getId(),
                    Collectors.mapping(k -> k.getKeyword().getLabel(), Collectors.toList())));

    List<ReviewResDTO.BestReviewItemDTO> items =
        reviews.stream()
            .map(
                review -> {
                  List<OrderItem> myOptions =
                      optionsByOrderId.getOrDefault(
                          review.getOrder().getId(), Collections.emptyList());
                  List<String> myKeywords =
                      keywordsByReviewId.getOrDefault(review.getId(), Collections.emptyList());

                  Long helpfulCount = likeCounts.getOrDefault(review.getId(), 0L);

                  return convertToBestReviewItemDTO(review, myOptions, myKeywords, helpfulCount);
                })
            .toList();

    return ReviewResDTO.BestReviewListDTO.builder().items(items).build();
  }

  private ReviewResDTO.BestReviewItemDTO convertToBestReviewItemDTO(
      Review review, List<OrderItem> options, List<String> keywords, Long helpfulCount) {

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

      switch (category) {
        case SHEET -> {
          sheetName = item.getOptionName();
          if (item.getColorRgbCode() != null) sheetColor = item.getColorRgbCode();
        }
        case CREAM -> {
          creamName = item.getOptionName();
          if (item.getColorRgbCode() != null) creamColor = item.getColorRgbCode();
        }
        case TOPPING -> decos.add(item.getOptionName());
        case ICING -> {
          if (item.getColorRgbCode() != null) icingColor = item.getColorRgbCode();
        }
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
            .additionalRequest(order.getOrderAdditionalRequest())
            .colors(colorsDTO)
            .build();

    String resultImageUrl = order.getReferenceImageUrl();

    return ReviewResDTO.BestReviewItemDTO.builder()
        .writerName(review.getUser().getNickname())
        .createdDate(review.getCreatedAt().format(formatter))
        .rating(review.getRating())
        .helpfulCount(helpfulCount)
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

  private List<String> extractImageUrlsForReview(
      Long reviewId, List<ReviewRow.MyReviewImageRow> imageRows) {

    return groupImageUrlsByReviewId(imageRows).getOrDefault(reviewId, List.of());
  }
}
