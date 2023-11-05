package ra.security.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.security.exception.ProductException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.Promotion;
import ra.security.model.dto.request.ProductRequestDTO;

import ra.security.model.dto.request.promotionDto.PromotionProductRequestDTO;
import ra.security.model.dto.response.ProductResponseDTO;
import ra.security.model.dto.response.PromotionResponseDTO;
import ra.security.service.IProductService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getPaginatedProducts(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String by) {


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(by), sort);
        List<ProductResponseDTO> products = (List<ProductResponseDTO>) productService.getProducts(name, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) throws ProductException {
        ProductResponseDTO product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid ProductRequestDTO requestDTO, BindingResult bindingResult) throws ProductException {

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        ProductResponseDTO createdProduct = productService.createProduct(requestDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody  ProductRequestDTO requestDTO
    ) throws ProductException {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, requestDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/lock/{id}")


    public ResponseEntity<String> lockUserAccount(@PathVariable Long id) throws ProductException {
        productService.lockProduct(id);
        return ResponseEntity.ok("Thiết lập trạng thái ngừng bán thành công");
    }
    @PutMapping("/unlock/{id}")


    public ResponseEntity<String> unlockUserAccount(@PathVariable Long id) throws ProductException {
     productService.unlockProduct(id);
        return ResponseEntity.ok("Thiết lập trạng thái mở bán thành công");

    }

//    @PutMapping("/removeCategory/productId/{id}")
//    public ResponseEntity<ProductResponseDTO> removeCategory(@PathVariable Long id) throws ProductException {
//        ProductResponseDTO product = productService.removeCategory(id);
//        return new ResponseEntity<>(product, HttpStatus.OK);
//    }
//
//    @PutMapping("/addCategory/categoryId/{categoryId}/to/productId/{productId}")
//    public ResponseEntity<ProductResponseDTO> addCategoryToProduct(
//            @PathVariable Long categoryId,
//            @PathVariable Long productId
//    ) throws ProductException {
//        ProductResponseDTO updatedProduct = productService.addCategoryToProduct(categoryId, productId);
//        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//    }

    @PutMapping("/addimg/to/{productId}")
    public ResponseEntity<ProductResponseDTO> addImageToProduct(
            @RequestParam("file") MultipartFile multipartFile,
            @PathVariable Long productId
    ) throws ProductException {
        ProductResponseDTO updatedProduct = productService.addImageToProduct(multipartFile, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
    @PutMapping("/productId/{productId}/delete/images/productImgId/{productImgId}")
    public ResponseEntity<ProductResponseDTO> deleteImageToProduct(
            @PathVariable Long productId,
            @PathVariable Long productImgId
    ) throws ProductException {
        ProductResponseDTO updatedProduct = productService.deleteImageToProduct(productId, productImgId);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
    @PutMapping("/add/event/productId/{productId}")
    public ResponseEntity<ProductResponseDTO> createPromotionForProduct(@PathVariable Long productId, @RequestBody @Valid PromotionProductRequestDTO promotion) throws PromotionException {
        // Thực hiện kiểm tra productId có hợp lệ và tạo khuyến mãi cho sản phẩm này
        ProductResponseDTO product = productService.createPromotionForProduct(productId, promotion);
        return ResponseEntity.ok(product);
    }
}
