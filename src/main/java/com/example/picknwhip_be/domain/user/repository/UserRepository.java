package com.example.picknwhip_be.domain.user.repository;

import com.example.picknwhip_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
