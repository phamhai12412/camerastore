package ra.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.security.model.domain.FavoriteProduct;
import ra.security.model.domain.Product;
import ra.security.model.domain.Users;

import java.util.List;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct,Long> {
    List<FavoriteProduct> findByUser_Id(Long user_id);
    List<FavoriteProduct> findByUserAndProduct(Users user, Product product);
}
