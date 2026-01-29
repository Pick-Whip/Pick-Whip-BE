package com.example.picknwhip_be.domain.custom.service;

import com.example.picknwhip_be.domain.custom.dto.CustomResDTO;
import java.util.List;

public interface CustomQueryService {

  List<CustomResDTO.GetDraftListDTO> findDraftList(Long userId);

  CustomResDTO.GetDraftDetailDTO findDraftDetail(Long id);

  CustomResDTO.DeleteDraftDTO deleteDraft(Long id, Long userId);
}
