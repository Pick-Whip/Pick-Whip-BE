package com.example.picknwhip_be.domain.favorite.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// TODO: 추후에 마이픽 케이크 디자인 구현 후 @Primary 삭제
@Component
@Primary
public class NoOpDesignMyPickChecker implements DesignMyPickChecker {

  @Override
  public Set<Long> findPickedDesignIds(Long userId, List<Long> designIds) {
    return Collections.emptySet();
  }
}
