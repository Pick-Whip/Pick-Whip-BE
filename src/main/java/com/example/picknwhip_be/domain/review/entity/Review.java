package com.example.picknwhip_be.domain.review.entity;

import com.example.picknwhip_be.domain.review.entity.mapping.ReviewImage;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewLike;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewReply;
import com.example.picknwhip_be.domain.review.entity.mapping.ReviewSelectedKeyword;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //    @OneToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "order_id", nullable = false, unique = true)
  //    private Order order;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "user_id", nullable = false)
  //    private User user;

  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "shop_id", nullable = false)
  //    private Shops shops;

  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "design_id")
  //    private DesignGallery design;

  @Column(name = "rating", nullable = false)
  private Integer rating;

  @Column(name = "content", length = 500, nullable = false)
  private String content;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
  private List<ReviewImage> images = new ArrayList<>();

  @OneToOne(mappedBy = "review")
  private ReviewReply reply;

  @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
  private List<ReviewSelectedKeyword> keywords = new ArrayList<>();

  @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
  private List<ReviewLike> likes = new ArrayList<>();

  public void softDelete(LocalDateTime now) {
    this.deletedAt = now;
  }
}
