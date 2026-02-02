package com.example.picknwhip_be.domain.review.service.user.service.query;

import com.example.picknwhip_be.domain.review.service.user.entity.User;

public interface UserQueryService {
  User getUser(Long userId);
}
