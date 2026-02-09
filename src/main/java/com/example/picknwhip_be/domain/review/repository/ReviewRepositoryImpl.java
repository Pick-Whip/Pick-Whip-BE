package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.design.entity.QDesignGallery;
import com.example.picknwhip_be.domain.design.enums.Style;
import com.example.picknwhip_be.domain.order.entity.QOrder;
import com.example.picknwhip_be.domain.review.dto.*;
import com.example.picknwhip_be.domain.review.entity.QReview;
import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewImage;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewKeyword;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewLike;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewReply;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewSelectedKeyword;
import com.example.picknwhip_be.domain.review.enums.ReviewSort;
import com.example.picknwhip_be.domain.review.service.query.cursor.ReviewCursor;
import com.example.picknwhip_be.domain.shop.entity.QShop;
import com.example.picknwhip_be.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  private static final QReview review = QReview.review;
  private static final QOrder order = QOrder.order;
  private static final QShop shop = QShop.shop;
  private static final QReviewImage reviewImage = QReviewImage.reviewImage;
  private static final QReviewReply reviewReply = QReviewReply.reviewReply;
  private static final QUser user = QUser.user;
  private static final QDesignGallery designGallery = QDesignGallery.designGallery;
  private static final QReviewKeyword reviewKeyword = QReviewKeyword.reviewKeyword;
  private static final QReviewLike reviewLike = QReviewLike.reviewLike;
  private static final QReviewSelectedKeyword reviewSelectedKeyword =
      QReviewSelectedKeyword.reviewSelectedKeyword;

  @Override
  public ReviewRow.ReviewSummaryRow fetchMyReviewSummary(Long userId) {
    return queryFactory
        .select(new QReviewRow_ReviewSummaryRow(review.id.count(), review.rating.avg()))
        .from(review)
        .where(review.user.userId.eq(userId), review.deletedAt.isNull())
        .fetchOne();
  }

  @Override
  public List<ReviewRow.MyReviewRow> fetchMyReviews(Long userId, Long cursor, int limit) {
    return queryFactory
        .select(
            new QReviewRow_MyReviewRow(
                review.id,
                shop.shopName,
                order.designGallery.designName,
                review.rating,
                review.content,
                review.createdAt))
        .from(review)
        .join(review.order, order)
        .join(order.shop, shop)
        .where(
            review.user.userId.eq(userId),
            cursor != null ? review.id.lt(cursor) : null,
            review.deletedAt.isNull())
        .orderBy(review.id.desc())
        .limit(limit)
        .fetch();
  }

  @Override
  public List<ReviewRow.MyReviewImageRow> fetchMyReviewImages(List<Long> reviewIds) {
    if (reviewIds == null || reviewIds.isEmpty()) {
      return List.of();
    }

    return queryFactory
        .select(
            new QReviewRow_MyReviewImageRow(
                reviewImage.review.id, reviewImage.s3Key, reviewImage.sortOrder))
        .from(reviewImage)
        .where(reviewImage.review.id.in(reviewIds), reviewImage.deletedAt.isNull())
        .orderBy(reviewImage.review.id.asc(), reviewImage.sortOrder.asc())
        .fetch();
  }

  @Override
  public List<ReviewRow.MyReviewReplyRow> fetchMyReviewReplies(List<Long> reviewIds) {
    if (reviewIds == null || reviewIds.isEmpty()) {
      return List.of();
    }

    return queryFactory
        .select(new QReviewRow_MyReviewReplyRow(reviewReply.review.id, reviewReply.content))
        .from(reviewReply)
        .where(reviewReply.review.id.in(reviewIds), reviewReply.deletedAt.isNull())
        .fetch();
  }

  @Override
  public ReviewRow.ReviewDetailRow fetchReviewDetail(Long reviewId) {
    return queryFactory
        .select(
            new QReviewRow_ReviewDetailRow(
                review.id,
                user.nickname,
                user.profileImageUrl,
                review.rating,
                review.content,
                review.createdAt,
                reviewReply.content))
        .from(review)
        .join(review.user, user)
        .leftJoin(reviewReply)
        .on(reviewReply.review.id.eq(review.id), reviewReply.deletedAt.isNull())
        .where(review.id.eq(reviewId), review.deletedAt.isNull())
        .fetchOne();
  }

  @Override
  public List<ReviewRow.KeywordRow> fetchReviewDetailKeywords(Long reviewId) {
    return queryFactory
        .select(
            new QReviewRow_KeywordRow(
                reviewSelectedKeyword.review.id, reviewKeyword.code, reviewKeyword.label))
        .from(reviewSelectedKeyword)
        .join(reviewSelectedKeyword.keyword, reviewKeyword)
        .where(reviewSelectedKeyword.review.id.eq(reviewId))
        .orderBy(reviewKeyword.id.asc())
        .fetch();
  }

  /**
   * 베스트 커스텀 리뷰 조회 삭제되지 않음, 공개 동의(agreement=true),디자인 갤러리 주문(designGallery != null) 도움이
   * 됐어요(helpfulCount) 내림차순 -> 최신순
   */
  @Override
  public List<Review> findBestHelpfulReviews(int limit) {
    List<Long> ids =
        queryFactory
            .select(review.id)
            .from(review)
            .join(review.order, order)
            .leftJoin(reviewLike)
            .on(reviewLike.review.eq(review))
            .where(
                review.deletedAt.isNull(),
                review.agreement.isTrue(),
                order.designGallery.isNotNull())
            .groupBy(review.id)
            .orderBy(reviewLike.count().desc(), review.id.desc())
            .limit(limit)
            .fetch();

    if (ids.isEmpty()) {
      return List.of();
    }

    List<Review> reviews =
        queryFactory
            .selectFrom(review)
            .join(review.order, order)
            .fetchJoin()
            .join(review.user, user)
            .fetchJoin()
            .join(order.designGallery, designGallery)
            .fetchJoin()
            .where(review.id.in(ids))
            .fetch();

    Map<Long, Review> reviewMap = reviews.stream().collect(Collectors.toMap(Review::getId, r -> r));

    return ids.stream().map(reviewMap::get).toList();
  }

  @Override
  public List<ReviewRow.ShopReviewRow> fetchShopReviewRows(
      Long shopId,
      ReviewSort sort,
      List<Long> designIds,
      List<Style> styles,
      ReviewCursor cursor,
      int limit) {
    boolean hasDesignFilter = designIds != null && !designIds.isEmpty();
    boolean hasStyleFilter = styles != null && !styles.isEmpty();
    boolean hasAnyFilter = hasDesignFilter || hasStyleFilter;

    BooleanBuilder where = new BooleanBuilder();

    where.and(review.shop.id.eq(shopId));
    where.and(review.deletedAt.isNull());

    if (hasAnyFilter) {
      where.and(review.design.isNotNull());
    }
    if (hasDesignFilter) {
      where.and(review.design.id.in(designIds));
    }
    if (hasStyleFilter) {
      where.and(review.design.keywords.any().in(styles));
    }

    // 비집계 커서 조건은 WHERE에 적용
    if (cursor != null && sort != ReviewSort.HELPFUL) {
      where.and(applyCursor(sort, cursor));
    }

    NumberExpression<Long> likeCount = reviewLike.id.count();
    OrderSpec orderSpec = orderSpec(sort, likeCount);

    JPAQuery<ReviewRow.ShopReviewRow> query =
        queryFactory
            .select(
                new QReviewRow_ShopReviewRow(
                    review.id,
                    user.nickname,
                    user.profileImageUrl,
                    review.rating,
                    designGallery.designName,
                    review.content,
                    likeCount.intValue(),
                    review.createdAt))
            .from(review)
            .join(review.user, user)
            .leftJoin(review.design, designGallery)
            .leftJoin(reviewLike)
            .on(reviewLike.review.eq(review))
            .where(where)
            .groupBy(
                review.id,
                user.nickname,
                user.profileImageUrl,
                review.rating,
                designGallery.designName,
                review.content,
                review.createdAt);

    // HELPFUL 커서 조건은 집계값이므로 HAVING에 적용
    if (cursor != null && sort == ReviewSort.HELPFUL) {
      long helpful = cursor.primaryValue();
      query.having(
          likeCount.lt(helpful).or(likeCount.eq(helpful).and(review.id.lt(cursor.reviewId()))));
    }

    return query.orderBy(orderSpec.primary, orderSpec.tieBreaker).limit(limit).fetch();
  }

  @Override
  public Set<Long> fetchLikedReviewIds(Long userId, List<Long> reviewIds) {
    if (userId == null || reviewIds == null || reviewIds.isEmpty()) {
      return Set.of();
    }

    QReviewLike reviewLike = QReviewLike.reviewLike;

    List<Long> likedIds =
        queryFactory
            .select(reviewLike.review.id)
            .from(reviewLike)
            .where(reviewLike.user.userId.eq(userId), reviewLike.review.id.in(reviewIds))
            .fetch();

    return new HashSet<>(likedIds);
  }

  @Override
  public List<ReviewRow.KeywordRow> fetchReviewKeywords(List<Long> reviewIds) {
    if (reviewIds == null || reviewIds.isEmpty()) {
      return List.of();
    }

    return queryFactory
        .select(
            new QReviewRow_KeywordRow(
                reviewSelectedKeyword.review.id, reviewKeyword.code, reviewKeyword.label))
        .from(reviewSelectedKeyword)
        .join(reviewSelectedKeyword.keyword, reviewKeyword)
        .where(reviewSelectedKeyword.review.id.in(reviewIds))
        .orderBy(reviewSelectedKeyword.review.id.asc(), reviewKeyword.id.asc())
        .fetch();
  }

  private record OrderSpec(OrderSpecifier<?> primary, OrderSpecifier<?> tieBreaker) {}

  private OrderSpec orderSpec(ReviewSort sort, NumberExpression<Long> likeCount) {
    return switch (sort) {
      case LATEST -> new OrderSpec(review.createdAt.desc(), review.id.desc());
      case HELPFUL -> new OrderSpec(likeCount.desc(), review.id.desc());
      case RATING_HIGH -> new OrderSpec(review.rating.desc(), review.id.desc());
      case RATING_LOW -> new OrderSpec(review.rating.asc(), review.id.asc());
    };
  }

  @Override
  public ReviewRow.ReviewSummaryRow fetchShopReviewSummary(Long shopId) {
    return queryFactory
        .select(new QReviewRow_ReviewSummaryRow(review.id.count(), review.rating.avg()))
        .from(review)
        .where(review.shop.id.eq(shopId), review.deletedAt.isNull())
        .fetchOne();
  }

  @Override
  public List<ReviewRow.KeywordCategoryCountRow> fetchShopKeywordCategoryCounts(Long shopId) {
    return queryFactory
        .select(
            new QReviewRow_KeywordCategoryCountRow(
                reviewKeyword.category, reviewSelectedKeyword.id.count()))
        .from(reviewSelectedKeyword)
        .join(reviewSelectedKeyword.keyword, reviewKeyword)
        .join(reviewSelectedKeyword.review, review)
        .where(review.shop.id.eq(shopId), review.deletedAt.isNull())
        .groupBy(reviewKeyword.category)
        .fetch();
  }

  /** 비집계 정렬(LATEST, RATING_HIGH, RATING_LOW)에 대한 커서 조건. HELPFUL은 having에서 처리. */
  private BooleanExpression applyCursor(ReviewSort sort, ReviewCursor cursor) {
    return switch (sort) {
      case LATEST ->
          review
              .createdAt
              .lt(cursor.createdAt())
              .or(review.createdAt.eq(cursor.createdAt()).and(review.id.lt(cursor.reviewId())));

      case RATING_HIGH -> {
        int rating = cursor.primaryValue().intValue();
        yield review
            .rating
            .lt(rating)
            .or(review.rating.eq(rating).and(review.id.lt(cursor.reviewId())));
      }

      case RATING_LOW -> {
        int rating = cursor.primaryValue().intValue();
        yield review
            .rating
            .gt(rating)
            .or(review.rating.eq(rating).and(review.id.gt(cursor.reviewId())));
      }

      case HELPFUL -> throw new IllegalStateException("HELPFUL cursor is handled via HAVING");
    };
  }
}
