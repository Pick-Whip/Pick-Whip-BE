package com.example.picknwhip_be.domain.order.repository;

import com.example.picknwhip_be.domain.custom.entity.OrderDraft;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDraftRepository extends JpaRepository<OrderDraft, Long> {
  List<OrderDraft> findAllByUserOrderByIdDesc(User user);
}
