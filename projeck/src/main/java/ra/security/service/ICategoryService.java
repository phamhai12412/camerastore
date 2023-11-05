package ra.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.security.exception.CategoryException;
import ra.security.exception.ProductException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.Category;
import ra.security.model.dto.request.CategoryRequestDto;
import ra.security.model.dto.request.promotionDto.PromotionCategoryRequestDTO;
import ra.security.model.dto.response.CategoryResponseDto;

import java.util.List;

public interface ICategoryService{
    CategoryResponseDto createPromotionForCategory(Long categoryId, PromotionCategoryRequestDTO promotion) throws PromotionException;

    CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) throws CategoryException;

    CategoryResponseDto getCategory(Long id) throws CategoryException;

    List<CategoryResponseDto> getCategories(String name, Pageable pageable);

    CategoryResponseDto editCategory(Long id, CategoryRequestDto categoryRequestDto) throws CategoryException;
    void lockCategorie(Long id) throws CategoryException;

    void unlockCategorie(Long id) throws CategoryException;
}
