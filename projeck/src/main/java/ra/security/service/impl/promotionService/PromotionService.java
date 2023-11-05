package ra.security.service.impl.promotionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.security.exception.PromotionException;

import ra.security.model.domain.Promotion;

import ra.security.model.dto.request.promotionDto.PromotionCodeRequestDTO;
import ra.security.model.dto.response.PromotionResponseDTO;

import ra.security.repository.PromotionRepository;
import ra.security.service.IPromotionService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService implements IPromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Override
    public PromotionResponseDTO createPromotionWithCode(PromotionCodeRequestDTO promotionCodeRequestDTO) throws PromotionException {
        Date currentDate = new Date();
        if (promotionCodeRequestDTO.getEndDate().before(promotionCodeRequestDTO.getStartDate()) || promotionCodeRequestDTO.getStartDate().before(currentDate)) {
            throw new PromotionException("Ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại và ngày kết thúc phải lớn hơn ngày bắt đầu");
        }

        Promotion promotion = Promotion.builder()
                .promotionCode(promotionCodeRequestDTO.getPromotionCode())
                .discountValue(promotionCodeRequestDTO.getDiscountValue())
                .startDate(promotionCodeRequestDTO.getStartDate())
                .endDate(promotionCodeRequestDTO.getEndDate())
                .build();
        return convertPromotionToResponseDTO(promotionRepository.save(promotion));
    }



    @Override

    public List<PromotionResponseDTO> getAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream()
                .map(this::convertPromotionToResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    public PromotionResponseDTO getPromotionById(Long id) throws PromotionException {

            Optional<Promotion> promotionOptional = promotionRepository.findById(id);

            if (promotionOptional.isPresent()) {
                Promotion promotion = promotionOptional.get();
                Date currentDate = new Date();

                if (promotion.getEndDate().before(currentDate)) {
                    throw new PromotionException("Mã giảm giá này đã hết hạn");
                } else {
                    return convertPromotionToResponseDTO(promotion);
                }
            } else {
                throw new PromotionException("Mã giảm giá này đã hết hạn hoặc không tồn tại");
            }
        }

    @Override
    public PromotionResponseDTO findByPromotionCode(String promotionCode) throws PromotionException {

        Optional<Promotion> promotionOptional = promotionRepository.findByPromotionCode(promotionCode);

        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();
            Date currentDate = new Date();

            if (promotion.getEndDate().before(currentDate)) {
                throw new PromotionException("Mã giảm giá đã hết hạn");
            } else {
                return convertPromotionToResponseDTO(promotion);
            }
        } else {
            throw new PromotionException("Không tìm thấy mã giảm giá này");
        }
    }

    @Override
    public PromotionResponseDTO deletePromotionById(Long id) throws PromotionException{
        Optional<Promotion> ProductOptional = promotionRepository.findById(id);
        if (ProductOptional.isPresent()) {
            Promotion promotion = ProductOptional.get();
            promotionRepository.delete(promotion);
            return convertPromotionToResponseDTO(promotion);
        } else {
            throw new PromotionException("Không tồn tại mã giảm giá này");
        }
    }
    public PromotionResponseDTO convertPromotionToResponseDTO(Promotion promotion) {
        PromotionResponseDTO responseDTO = new PromotionResponseDTO();
        responseDTO.setId(promotion.getId());
        responseDTO.setPromotionCode(promotion.getPromotionCode());
        responseDTO.setDiscountValue(promotion.getDiscountValue());
        responseDTO.setStartDate(promotion.getStartDate());
        responseDTO.setEndDate(promotion.getEndDate());
        responseDTO.setQuantity(promotion.getQuantity());

        return responseDTO;
    }
}
