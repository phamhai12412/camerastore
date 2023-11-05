package ra.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.security.model.domain.OrderStatus;
import ra.security.model.domain.Product;
import ra.security.model.domain.Users;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCaseAndStatus(String name, boolean status, Pageable pageable);
    List<Product> findAllByCategory_Id(Long id);
    Page<Product> findAllByStatus(boolean status, Pageable pageable);


}
