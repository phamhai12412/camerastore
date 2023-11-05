package ra.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.security.model.domain.CartItem;
import ra.security.model.domain.OrderStatus;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("select ci from CartItem ci inner join ci.order o where o.status = :orderStatus and o.user.id = :userId")

    List<CartItem> findByOrderStatusAndUserId(@Param("orderStatus") OrderStatus orderStatus,
                                              @Param("userId") Long userId);
}
