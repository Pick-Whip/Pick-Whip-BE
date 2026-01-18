package com.example.picknwhip_be.domain.user.service;

import com.example.picknwhip_be.domain.user.entity.User;

public interface UserQueryService {
  User getUser(Long userId);
}
