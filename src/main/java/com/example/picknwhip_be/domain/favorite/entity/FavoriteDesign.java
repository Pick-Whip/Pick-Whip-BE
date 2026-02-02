package com.example.picknwhip_be.domain.favorite.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.review.service.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
    name = "favorite_designs",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_favorite_design_user",
          columnNames = {"user_id", "design_id"})
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FavoriteDesign {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "design_id", nullable = false)
  private DesignGallery designGallery;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
}
