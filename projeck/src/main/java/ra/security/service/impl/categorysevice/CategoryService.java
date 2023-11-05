package ra.security.service.impl.categorysevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.security.exception.CategoryException;
import ra.security.exception.ProductException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.Category;

import ra.security.model.domain.Product;
import ra.security.model.dto.request.CategoryRequestDto;
import ra.security.model.dto.request.promotionDto.PromotionCategoryRequestDTO;
import ra.security.model.dto.response.CategoryResponseDto;
import ra.security.repository.CategoryRepository;
import ra.security.repository.ProductRepository;
import ra.security.service.ICategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

@Autowired
private ProductRepository productRepository;

    @Autowired
    private  CategoryRepository categoryRepository;
@Override
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) throws CategoryException {
    if(categoryRepository.findByName(categoryRequestDto.getName())!=null){
        throw new CategoryException("Đã tồn tại danh mục này");

    }
        Category category = new Category();
        category.setName(categoryRequestDto.getName());


        Category savedCategory = categoryRepository.save(category);

        return converDto(savedCategory);
    }
    @Override
    public CategoryResponseDto getCategory(Long id) throws CategoryException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return converDto(category);
        } else {

           throw  new CategoryException("không tìm thấy danh mục");
        }
    }
    @Override
    public List<CategoryResponseDto> getCategories(String name, Pageable pageable) {
        Page<Category> categories;
        if (name.isEmpty()) {
            categories = categoryRepository.findAll(pageable);
        } else {
            categories = categoryRepository.findAllByNameContainingIgnoreCase(name, pageable);
        }

        return categories.map(this::converDto).getContent();
    }
    @Override
    public void lockCategorie(Long id) throws CategoryException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("Không tìm thấy danh mục"));


        category.setStatus(false);
        categoryRepository.save(category);
    }

    @Override
    public void unlockCategorie(Long id) throws CategoryException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("Không tìm thấy danh mục"));


        category.setStatus(true);
        categoryRepository.save(category);
    }
    @Override
    public CategoryResponseDto editCategory(Long id, CategoryRequestDto categoryRequestDto) throws CategoryException{
        if(categoryRepository.findByName(categoryRequestDto.getName())!=null){
            throw new CategoryException("Đã tồn tại danh mục này");

        }
    Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            if(categoryRequestDto.getName()!=null){
            category.setName(categoryRequestDto.getName());}
            Category updatedCategory = categoryRepository.save(category);
            return converDto(updatedCategory);
        } else {
            throw  new CategoryException("không tìm thấy danh mục");
        }
    }
    public CategoryResponseDto createPromotionForCategory(Long categoryId, PromotionCategoryRequestDTO promotion) throws PromotionException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setDiscountevent(promotion.getDiscountValue());
            List<Product> products=productRepository.findAllByCategory_Id(categoryId);
            for (Product p:products
                 ) {
                p.setDiscountevent(promotion.getDiscountValue());
            }
            return converDto(categoryRepository.save(category));
        }
        throw new PromotionException("Không tồn tại danh mục này");
    }
    private CategoryResponseDto
    converDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .discountevent(category.getDiscountevent())
                .status(category.isStatus())
                .build();
    }

}
