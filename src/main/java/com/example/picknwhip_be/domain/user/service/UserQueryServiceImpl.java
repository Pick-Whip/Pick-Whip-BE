package com.example.picknwhip_be.domain.user.service;

import com.example.picknwhip_be.domain.user.entity.User;
import com.example.picknwhip_be.domain.user.exception.UserException;
import com.example.picknwhip_be.domain.user.exception.code.UserErrorCode;
import com.example.picknwhip_be.domain.user.repository.UserRepository;
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
        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
  }
}
