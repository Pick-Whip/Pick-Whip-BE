package com.example.picknwhip_be.domain.custom.service.query;

import com.example.picknwhip_be.domain.custom.dto.res.CustomResDTO;
import java.util.List;

public interface CustomQueryService {

  List<CustomResDTO.GetDraftListDTO> findDraftList(Long userId);

  CustomResDTO.GetDraftDetailDTO findDraftDetail(Long id);

  CustomResDTO.DeleteDraftDTO deleteDraft(Long id, Long userId);

  CustomResDTO.GetDesignOptionDTO findDesignOption(Long shopId);
}
