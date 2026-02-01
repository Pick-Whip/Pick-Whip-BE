package com.example.picknwhip_be.domain.design.service;

import com.example.picknwhip_be.domain.design.dto.DesignResDTO;
import com.example.picknwhip_be.domain.design.repository.DesignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DesignQueryServiceImpl implements DesignQueryService {

  private final DesignRepository designRepository;

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignListDTO findDesignListByUserId(Long Id) {

    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public DesignResDTO.GetDesignListDTO findDesignListByShopId(Long Id) {

    return null;
  }
}
