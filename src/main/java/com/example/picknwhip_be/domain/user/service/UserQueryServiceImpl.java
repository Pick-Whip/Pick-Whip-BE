package com.example.picknwhip_be.domain.user.service;

import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.entity.UserStatus;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
import com.example.picknwhip_be.global.apiPayload.code.GeneralErrorCode;
import com.example.picknwhip_be.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

  private final UserRepository userRepository;

  @Override
  public User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .filter(user -> user.getStatus() == UserStatus.ACTIVE)
        .orElseThrow(() -> new GeneralException(GeneralErrorCode.USER_NOT_FOUND));
  }
}
