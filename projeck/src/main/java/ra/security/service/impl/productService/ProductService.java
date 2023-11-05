package ra.security.service.impl.productService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.security.exception.ProductException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.*;
import ra.security.model.dto.request.ProductRequestDTO;
import ra.security.model.dto.request.promotionDto.PromotionProductRequestDTO;
import ra.security.model.dto.response.ProductImgResponseDTO;
import ra.security.model.dto.response.ProductResponseDTO;
import ra.security.model.dto.response.PromotionResponseDTO;
import ra.security.repository.CategoryRepository;
import ra.security.repository.ManufacturerRepository;
import ra.security.repository.ProductImgRepository;
import ra.security.repository.ProductRepository;
import ra.security.service.IProductService;
import ra.security.service.impl.storageservice.StorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService  implements IProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImgRepository productImgRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    public List<ProductResponseDTO> getProducts(String name, Pageable pageable) {
        Page<Product> products;
        if (name.isEmpty()) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findAllByNameContainingIgnoreCase(name, pageable);
        }

        return products.map(this::convertToResponseDTO).getContent();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) throws ProductException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return convertToResponseDTO(optionalProduct.get());
        }
        throw new ProductException("Không tìm thấy sản phẩm!");
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) throws ProductException{
        Product product = convertToEntity(requestDTO);
        List<String> listUrl = new ArrayList<>();
        if (requestDTO.getFile() != null) {
            for (MultipartFile m : requestDTO.getFile()) {
                listUrl.add(storageService.uploadFile(m));
            }
            // set image active vào cái ảnh đầu tiên
            product.setImgurl(listUrl.get(0));
            List<ProductImg> images = new ArrayList<>();
            for (String url : listUrl) {
                images.add(ProductImg.builder().productUrl(url).product(product).build());
            }
            // set list image vào product
            product.setProductImgList(images);
        }
        ProductResponseDTO productResponseDTO = convertToResponseDTO(productRepository.save(product));
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) throws ProductException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (requestDTO.getName() != null) {
                product.setName(requestDTO.getName());
            }

            if (requestDTO.getDescription() != null) {
                product.setDescription(requestDTO.getDescription());
            }

            if (requestDTO.getPrice()>0) {
                product.setPrice(requestDTO.getPrice());
            }

            if (requestDTO.getQuantity() > 0) {
                product.setQuantity(requestDTO.getQuantity());
            }
            if (requestDTO.getCategoryId() != null) {
                Category category=   categoryRepository.findById(requestDTO.getCategoryId()).orElseThrow(()->new ProductException("chỉnh sửa thất bại không tồn tại danh mục này"));
                if(!category.isStatus()){
                    throw new ProductException("Bạn đã khóa danh mục này hãy kiểm tra lại");
                }else {
                    product.setCategory(category);
                }


            }

            if (requestDTO.getManufacturerId() != null) {
                Manufacturer manufacturer=   manufacturerRepository.findById(requestDTO.getManufacturerId()).orElseThrow(()->new ProductException("chỉnh sửa thất bại không tồn tại hãng sản xuất này này"));
                if(!manufacturer.isStatus()){
                    throw new ProductException("Bạn đã khóa hãng sản xuất này hãy kiểm tra lại");
                }else {
                    product.setManufacturer(manufacturer);
                }


            }

            Product updatedProduct = productRepository.save(product);
            return convertToResponseDTO(updatedProduct);
        } else {
            throw new ProductException("Không tồn tại sản phẩm này");
        }
    }
    @Override
    public ProductResponseDTO lockProduct(Long id) throws ProductException{
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Không tìm thấy sản phẩm"));


            product.setStatus(false);
        return convertToResponseDTO( productRepository.save(product));
    }

    @Override
    public ProductResponseDTO unlockProduct(Long id) throws ProductException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Không tìm thấy sản phẩm"));


        product.setStatus(true);

        return convertToResponseDTO( productRepository.save(product));
    }

    @Override
    public ProductResponseDTO addCategoryToProduct(Long categoryId, Long productId) throws ProductException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if (!optionalProduct.isPresent()) {
            throw new ProductException("Không tồn tại sản phẩm.");
        }

        if (!categoryOptional.isPresent()) {
            throw new ProductException("Danh mục không tồn tại.");
        }

        Product product = optionalProduct.get();
        product.setCategory(categoryOptional.get());
        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }


    @Override
    public ProductResponseDTO removeCategory(Long productId) throws ProductException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new ProductException("Không tồn tại sản phẩm.");
        }
        Product product=optionalProduct.get();
        product.setCategory(null);
        return convertToResponseDTO(productRepository.save(product));
    }

    private Product convertToEntity(ProductRequestDTO requestDTO) throws ProductException {
        Product product = new Product();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setQuantity(requestDTO.getQuantity());

//         Xác định Category và Manufacturer dựa trên categoryId và manufacturerId
        if (requestDTO.getCategoryId() != null) {
         Category category=   categoryRepository.findById(requestDTO.getCategoryId()).orElseThrow(()->new ProductException("không tồn tại danh mục"));
            if(!category.isStatus()){
                throw new ProductException("Bạn đã khóa danh mục này hãy kiểm tra lại");
            }else {
                product.setCategory(category);
            }


        }

        if (requestDTO.getManufacturerId() != null) {
            Manufacturer manufacturer=   manufacturerRepository.findById(requestDTO.getManufacturerId()).orElseThrow(()->new ProductException("không tồn tại hãng sản xuất này"));
            if(!manufacturer.isStatus()){
                throw new ProductException("Bạn đã khóa hãng sản xuất này hãy kiểm tra lại");
            }else {
                product.setManufacturer(manufacturer);
            }


        }
        return product;
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        List<ProductImgResponseDTO> productImgList = product.getProductImgList()
                .stream()
                .map(productImg -> ProductImgResponseDTO.builder()
                        .id(productImg.getId())
                        .productUrl(productImg.getProductUrl())
                        .build())
                .collect(Collectors.toList());
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .priceevent(product.getPrice()*(1- (double) (product.getDiscountevent()) /100))
                .quantity(product.getQuantity())
                .imgurl(product.getImgurl())
                .discountevent(product.getDiscountevent())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryname(product.getCategory() != null ? product.getCategory().getName() : null)
                .manufacturerId(product.getManufacturer() != null ? product.getManufacturer().getId() : null)
                .manufacturername(product.getManufacturer() != null ? product.getManufacturer().getName() : null)
                .productImgList(productImgList)
                .status(product.isStatus())
                .build();
    }





    @Override
    public ProductResponseDTO addImageToProduct(MultipartFile multipartFile, Long productId) throws ProductException{
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Không tìm thấy sản phẩm"));
        String url = storageService.uploadFile(multipartFile);
        product.getProductImgList().add(ProductImg.builder().productUrl(url).product(product).build());
        return convertToResponseDTO(productRepository.save(product));
    }


    @Override
    public ProductResponseDTO deleteImageToProduct(Long productId, Long productImgID) throws ProductException {
        // Tìm sản phẩm và ảnh sản phẩm cần xóa
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Không tìm thấy sản phẩm"));

       productImgRepository.findById(productImgID)
                .orElseThrow(() -> new ProductException("Không tìm thấy hình ảnh sản phẩm"));
        productImgRepository.deleteByProductIdAndProductImgId(productId,productImgID);

        return convertToResponseDTO(product);
    }
    public ProductResponseDTO createPromotionForProduct(Long productId, PromotionProductRequestDTO promotion) throws PromotionException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
product.setDiscountevent(promotion.getDiscountValue());
            return convertToResponseDTO(productRepository.save(product));
        }
        throw new PromotionException("Không tồn tại sản phẩm này");
    }


}
