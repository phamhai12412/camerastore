package ra.security.service;

import ra.security.exception.PromotionException;
import ra.security.model.domain.Promotion;
import ra.security.model.dto.request.promotionDto.PromotionCodeRequestDTO;
import ra.security.model.dto.response.PromotionResponseDTO;

import java.util.List;

public interface IPromotionService {

    PromotionResponseDTO createPromotionWithCode(PromotionCodeRequestDTO promotion) throws PromotionException;

    List<PromotionResponseDTO> getAllPromotions();

    PromotionResponseDTO getPromotionById(Long id) throws PromotionException;


    PromotionResponseDTO deletePromotionById(Long id) throws PromotionException;
    PromotionResponseDTO findByPromotionCode(String promotionCode) throws PromotionException;
}
