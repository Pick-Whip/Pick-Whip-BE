package com.example.picknwhip_be.domain.review.service.user.repository;

import com.example.picknwhip_be.domain.review.service.user.entity.WithdrawalReasonItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalReasonItemRepository extends JpaRepository<WithdrawalReasonItem, Long> {}
