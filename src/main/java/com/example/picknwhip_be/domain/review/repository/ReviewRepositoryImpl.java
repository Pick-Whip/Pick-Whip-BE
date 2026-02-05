package com.example.picknwhip_be.domain.review.repository;

import com.example.picknwhip_be.domain.design.entity.QDesignGallery;
import com.example.picknwhip_be.domain.order.entity.QOrder;
import com.example.picknwhip_be.domain.review.dto.*;
import com.example.picknwhip_be.domain.review.entity.QReview;
import com.example.picknwhip_be.domain.review.entity.Review;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewImage;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewKeyword;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewLike;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewReply;
import com.example.picknwhip_be.domain.review.entity.mapping.QReviewSelectedKeyword;
import com.example.picknwhip_be.domain.shop.entity.QShop;
import com.example.picknwhip_be.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
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
  public ReviewRow.MyReviewSummaryRow fetchMyReviewSummary(Long userId) {
    return queryFactory
        .select(new QReviewRow_MyReviewSummaryRow(review.id.count(), review.rating.avg()))
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
}
