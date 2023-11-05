package ra.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ra.security.exception.ProductException;
import ra.security.exception.PromotionException;
import ra.security.model.dto.request.ProductRequestDTO;
import ra.security.model.dto.request.promotionDto.PromotionProductRequestDTO;
import ra.security.model.dto.response.ProductResponseDTO;

import java.util.List;

public interface IProductService {
    List<ProductResponseDTO> getProducts(String name, Pageable pageable);

    ProductResponseDTO getProductById(Long id) throws ProductException;

    ProductResponseDTO createProduct(ProductRequestDTO requestDTO) throws ProductException;

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) throws ProductException;

    ProductResponseDTO lockProduct(Long id) throws ProductException;

    ProductResponseDTO unlockProduct(Long id) throws ProductException;

    ProductResponseDTO createPromotionForProduct(Long productId, PromotionProductRequestDTO promotion) throws PromotionException;
    ProductResponseDTO addCategoryToProduct(Long categoryId, Long productId) throws ProductException;

    ProductResponseDTO removeCategory(Long productId) throws ProductException;

    ProductResponseDTO addImageToProduct(MultipartFile multipartFile, Long productId) throws ProductException;


    ProductResponseDTO deleteImageToProduct(Long productId, Long productImgId) throws ProductException;
}
