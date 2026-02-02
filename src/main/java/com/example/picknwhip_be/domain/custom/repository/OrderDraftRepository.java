package com.example.picknwhip_be.domain.custom.repository;

import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.review.service.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDraftRepository extends JpaRepository<OrderDraft, Long> {

  @EntityGraph(attributePaths = {"items", "items.customOption", "shopCakeSize"})
  List<OrderDraft> findAllByUserOrderByIdDesc(User user);
}
