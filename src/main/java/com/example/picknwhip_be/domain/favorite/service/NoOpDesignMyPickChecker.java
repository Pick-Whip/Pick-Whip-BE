package com.example.picknwhip_be.domain.favorite.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class NoOpDesignMyPickChecker implements DesignMyPickChecker {

  @Override
  public Set<Long> findPickedDesignIds(Long userId, List<Long> designIds) {
    return Collections.emptySet();
  }
}
