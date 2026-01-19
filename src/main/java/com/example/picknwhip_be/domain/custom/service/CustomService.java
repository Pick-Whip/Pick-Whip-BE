package com.example.picknwhip_be.domain.custom.service;

import com.example.picknwhip_be.domain.custom.dto.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;

public interface CustomService {

  CustomResDTO.CustomCreateDTO saveCustom(CustomReqDTO.CustomCreateDTO dto);
}
