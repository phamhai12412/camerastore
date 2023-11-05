package ra.security.service.impl.favoriteProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.security.exception.ProductException;
import ra.security.model.domain.FavoriteProduct;
import ra.security.model.domain.Product;
import ra.security.model.domain.Users;
import ra.security.model.dto.response.FavoriteProductResponseDto;
import ra.security.repository.FavoriteProductRepository;
import ra.security.repository.IUserRepository;
import ra.security.repository.ProductRepository;
import ra.security.service.IFavoriteProductService;


import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class
FavoriteProductService implements IFavoriteProductService {
    @Autowired

    private  FavoriteProductRepository favoriteProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<FavoriteProductResponseDto> getUserFavoriteProducts(String username) throws ProductException {
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new ProductException("không tìm thấy use"));

        } else {
            throw new ProductException("token xác thực không hợp lệ");
        }

       return favoriteProductRepository.findByUser_Id(users.getId()).stream().map(favoriteProduct -> coverdto(favoriteProduct)).collect(Collectors.toList());
    }

    @Override
    public void addProductToFavorites(String username, Long productId) throws ProductException{
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new ProductException("không tìm thấy use"));

        } else {
            throw new ProductException("token xác thực không hợp lệ");
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductException("Sản phẩm không tồn tại");
        }


        List<FavoriteProduct> favoriteProductList = favoriteProductRepository.findByUserAndProduct(users, product);
        if (!favoriteProductList.isEmpty()) {
            throw new ProductException("Đã nằm trong danh sách yêu thích");
        }

        // Thêm sản phẩm vào danh sách yêu thích của người dùng
        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setUser(users);
        favoriteProduct.setProduct(product);
        favoriteProduct.setFavoriteTime(new Date());
        favoriteProductList.add(favoriteProduct);
        favoriteProductRepository.save(favoriteProduct);
        users.setFavoriteProducts(favoriteProductList);
    }

    @Override
    public void removeProductFromFavorites(String username, Long productId) throws ProductException{

        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new ProductException("không tìm thấy use"));

        } else {
            throw new ProductException("token xác thực không hợp lệ");
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductException("Sản phẩm không tồn tại");
        }
        List<FavoriteProduct> productList = favoriteProductRepository.findByUserAndProduct(users, product);
    int check=-1;
        if (!productList.isEmpty()) {
            for (FavoriteProduct f:productList
                 ) {
                if(f.getProduct().getId()==productId){
                    productList.remove(f);
                    users.setFavoriteProducts(productList);
                    favoriteProductRepository.delete(f);
                    userRepository.save(users);
                    check=1;
                    break;

                }
            }
        }
        if(check==-1)
       {
            throw new ProductException("sản phẩm chưa nằm trong danh sách yêu thích");
        }
    }

        public FavoriteProductResponseDto coverdto(FavoriteProduct favoriteProduct) {
            FavoriteProductResponseDto dto = new FavoriteProductResponseDto();
            dto.setId(favoriteProduct.getId());
            dto.setUsername(favoriteProduct.getUser().getUsername());
            dto.setProductID(favoriteProduct.getProduct().getId());
            dto.setProductname(favoriteProduct.getProduct().getName());
            dto.setFavoriteTime(favoriteProduct.getFavoriteTime());

            return dto;
        }


}
