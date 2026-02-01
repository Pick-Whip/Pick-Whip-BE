package com.example.picknwhip_be.domain.design.entity;

import com.example.picknwhip_be.domain.design.entity.mapping.DesignOption;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringAlignment;
import com.example.picknwhip_be.domain.order.entity.enums.LetteringLineCount;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.shop.entity.ShopCakeSize;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "design_gallery")
public class DesignGallery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shop_cake_size_id", nullable = false)
  private ShopCakeSize shopCakeSize;

  @Column(name = "design_name", nullable = false)
  private String designName;

  @Column(name = "base_price", nullable = false)
  private int basePrice;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "allergy_info")
  private String allergyInfo;

  @Column(name = "description")
  private String description;

  @Column(name = "lettering_text", length = 30)
  private String letteringText;

  @Enumerated(EnumType.STRING)
  @Column(name = "lettering_line_count")
  private LetteringLineCount letteringLineCount;

  @Enumerated(EnumType.STRING)
  @Column(name = "lettering_alignment")
  private LetteringAlignment letteringAlignment;

  @ElementCollection
  @CollectionTable(
      name = "design_gallery_keywords",
      joinColumns = @JoinColumn(name = "design_gallery_id"))
  @Column(name = "keyword")
  private List<String> keywords;

  @OneToMany(mappedBy = "designGallery", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DesignOption> options = new ArrayList<>();
}
