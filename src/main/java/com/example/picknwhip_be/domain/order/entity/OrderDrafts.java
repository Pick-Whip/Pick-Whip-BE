package com.example.picknwhip_be.domain.order.entity;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "orders_drafts")
public class OrderDrafts extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "user_id", nullable = false)
  //    private Users users;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "shop_id", nullable = false)
  //    private Shops shops;

  //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //    @JoinColumn(name = "shop_cake_size_id", nullable = false)
  //    private Shops shops;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "design_id", nullable = false)
  private DesignGallery designGallery;

  @Column(name = "pickup_datetime")
  private LocalDateTime pickupDatetime;

  @Column(name = "lettering_text", length = 30)
  private String letteringText;

  @Enumerated(EnumType.STRING)
  @Column(name = "lettering_line_count")
  private LetteringLineCount letteringLineCount;

  @Column(name = "lettering_alignment")
  private LetteringAlignment letteringAlignment;

  @Column(name = "additional_request")
  private String additionalRequest;

  @Column(name = "reference_image_url")
  private String referenceImageUrl;
}
