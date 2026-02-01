package com.example.picknwhip_be.domain.design.repository;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignGalleryRepository extends JpaRepository<DesignGallery, Long> {

    List<DesignGallery> findByShopId(Long shopId);
}
