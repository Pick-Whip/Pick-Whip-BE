package com.example.picknwhip_be.domain.favorite.service;

import java.util.List;
import java.util.Set;

public interface DesignMyPickCheckerService {
  Set<Long> findPickedDesignIds(Long userId, List<Long> designIds);
}
