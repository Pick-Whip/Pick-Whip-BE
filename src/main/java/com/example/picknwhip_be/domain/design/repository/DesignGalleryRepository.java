package com.example.picknwhip_be.domain.design.repository;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignGalleryRepository extends JpaRepository<DesignGallery, Long> {

  @EntityGraph(attributePaths = {"options", "options.customOption", "shopCakeSize", "keywords"})
  Optional<DesignGallery> findDesignGalleryById(Long id);

  @EntityGraph(attributePaths = {"keywords"})
  List<DesignGallery> findByShopId(Long shopId);
}
