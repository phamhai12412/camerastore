package ra.security.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.CategoryException;
import ra.security.exception.ProductException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.Category;
import ra.security.model.dto.request.CategoryRequestDto;
import ra.security.model.dto.request.promotionDto.PromotionCategoryRequestDTO;
import ra.security.model.dto.response.CategoryResponseDto;
import ra.security.service.ICategoryService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/admin/category")
@CrossOrigin("*")
public class CategoryController {
@Autowired
    private ICategoryService categoryService;
    @PostMapping("/add")

    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) throws CategoryException {
        CategoryResponseDto createdCategory = categoryService.addCategory(categoryRequestDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")

    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable Long id) throws CategoryException {
        CategoryResponseDto category = categoryService.getCategory(id);
            return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping()

    public ResponseEntity<List<CategoryResponseDto>> getCategories(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String by) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(by), sort);
        List<CategoryResponseDto> categories = categoryService.getCategories(name, pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<CategoryResponseDto> editCategory(
            @RequestBody CategoryRequestDto categoryRequestDto,
            @PathVariable Long id) throws CategoryException {
        CategoryResponseDto updatedCategory = categoryService.editCategory(id, categoryRequestDto);

            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

    }
    @PutMapping("/addpromotion/{categoryId}")
    public ResponseEntity<CategoryResponseDto> createPromotionForCategory(@PathVariable Long categoryId, @RequestBody @Valid PromotionCategoryRequestDTO promotion) throws PromotionException {
        // Thực hiện kiểm tra categoryId có hợp lệ và tạo khuyến mãi cho danh mục này
        CategoryResponseDto category=categoryService.createPromotionForCategory(categoryId,promotion);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/lock/{id}")


    public ResponseEntity<String> lockUserAccount(@PathVariable Long id) throws CategoryException {
        categoryService.lockCategorie(id);
        return ResponseEntity.ok("Thiết lập trạng thái tạm khóa thành công");
    }
    @PutMapping("/unlock/{id}")


    public ResponseEntity<String> unlockUserAccount(@PathVariable Long id) throws CategoryException {
        categoryService.unlockCategorie(id);
        return ResponseEntity.ok("Thiết lập trạng thái mở khóa thành công");
    }
}
