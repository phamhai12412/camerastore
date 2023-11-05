package ra.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.security.model.domain.ProductImg;

import javax.transaction.Transactional;

public interface ProductImgRepository extends JpaRepository<ProductImg,Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ProductImg pi WHERE pi.product.id = :productId AND pi.id = :productImgId")
    void deleteByProductIdAndProductImgId(@Param("productId") Long productId, @Param("productImgId") Long productImgId);
}
