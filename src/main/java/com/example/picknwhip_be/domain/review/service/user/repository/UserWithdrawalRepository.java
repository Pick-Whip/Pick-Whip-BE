package com.example.picknwhip_be.domain.review.service.user.repository;

import com.example.picknwhip_be.domain.review.service.user.entity.UserWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWithdrawalRepository extends JpaRepository<UserWithdrawal, Long> {}
