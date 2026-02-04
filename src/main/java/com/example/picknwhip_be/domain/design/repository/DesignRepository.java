package com.example.picknwhip_be.domain.design.repository;

import com.example.picknwhip_be.domain.design.entity.DesignGallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignRepository extends JpaRepository<DesignGallery, Long> {}
