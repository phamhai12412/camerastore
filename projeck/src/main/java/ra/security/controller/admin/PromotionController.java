package ra.security.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.PromotionException;
import ra.security.model.dto.request.promotionDto.PromotionCodeRequestDTO;
import ra.security.model.dto.response.PromotionResponseDTO;
import ra.security.service.IPromotionService;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
@CrossOrigin("*")
public class PromotionController {
    @Autowired
    private IPromotionService promotionService;


    @PostMapping("/add")
    public ResponseEntity<PromotionResponseDTO> createPromotionWithCode(@RequestBody @Valid PromotionCodeRequestDTO promotion) throws PromotionException {
        // Tạo khuyến mãi với mã giảm giá
        // Sử dụng promotionService để lưu trữ thông tin khuyến mãi
        PromotionResponseDTO  createdPromotion = promotionService.createPromotionWithCode(promotion);
        return new ResponseEntity<>(createdPromotion,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<PromotionResponseDTO>> getAllPromotions() {
        // Lấy danh sách tất cả các khuyến mãi
        List<PromotionResponseDTO> promotions = promotionService.getAllPromotions();
        return new ResponseEntity<>(promotions,HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<PromotionResponseDTO> getPromotionById(@PathVariable Long id) throws PromotionException {
        // Lấy thông tin chi tiết của một khuyến mãi bằng ID
        PromotionResponseDTO promotion = promotionService.getPromotionById(id);
        return new ResponseEntity<>(promotion,HttpStatus.OK);

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PromotionResponseDTO> deletePromotionById(@PathVariable Long id) throws PromotionException {
        // Xóa khuyến mãi bằng ID
        PromotionResponseDTO promotion = promotionService.deletePromotionById(id);
        return new ResponseEntity<>(promotion,HttpStatus.OK);

    }
}
