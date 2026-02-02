package com.example.picknwhip_be.domain.custom.service.command;

import com.example.picknwhip_be.domain.custom.dto.req.CustomReqDTO;
import com.example.picknwhip_be.domain.custom.dto.res.CustomResDTO;

public interface CustomCommandService {

  CustomResDTO.CustomCreateDTO saveCustom(Long userId, CustomReqDTO.CustomCreateDTO dto);
}
