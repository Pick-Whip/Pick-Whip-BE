package com.example.picknwhip_be.domain.user.repository;

import com.example.picknwhip_be.domain.user.entity.WithdrawalReasonItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalReasonItemRepository extends JpaRepository<WithdrawalReasonItem, Long> {}
