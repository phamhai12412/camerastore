package ra.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.security.model.domain.Order;
import ra.security.model.domain.OrderStatus;
import ra.security.model.domain.Product;
import ra.security.model.domain.Users;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    // lấy tổng doanh thu cho tháng hiện tại
    @Query("select sum(o.totalAmount) from Order o where year (o.orderDate) = year (current_date ) and month (o.orderDate) = month (current_date ) and o.status = 'DELIVERED'")

    Double getTotalRevenueForCurrentMonth();
    // lấy tổng doanh thu cho năm hiện tại
    @Query("select sum (o.totalAmount) from Order o where year (o.orderDate) = year (current_date ) and o.status = 'DELIVERED'")
    Double getTotalRevenueForCurrentYear();
    @Query("select ci.product from CartItem ci join ci.order o where o.status = :status and YEAR(o.orderDate) = YEAR(current_date) and MONTH(o.orderDate) = MONTH(current_date) group by  ci.product order by SUM(ci.quantity) desc ")

    Page<Product> findTop5SellingProducts(@Param("status") OrderStatus status, Pageable pageable);

    @Query("select ci.product from Review ci where year (ci.datetime) = year (current_date) and MONTH(ci.datetime) = MONTH(current_date) group by ci.product order by AVG(ci.rating) desc ")

    Page<Product> getTop5ReviewedProducts(Pageable pageable);
    @Query("select r.product from FavoriteProduct r group by r.product order by count (r) desc ")

    Page<Product> getTop5FavoriteProducts(Pageable pageable);
   Order findByStatusAndUser_Id(OrderStatus status, Long userId);
   Order findByIdAndUser_Id(Long id, Long user_id);
    List<Order> findAllByStatusAndUser_Id(OrderStatus status, Long user_id);
    List<Order> findAllByStatus(OrderStatus status);

    @Query("select distinct p from Order o join o.cartItems ci join ci.product p where o.status = :orderStatus and o.user.id = :userId")
    List<Product> findProductsFromDeliveredOrdersByUserId(
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("userId") Long userId
    );

    @Query("select o from Order o where o.status <> 'CART' and o.user.name = :userName")
    List<Order> listOrderByUsernameAndStatusNotCart(@Param("userName") String userName);

    @Query("select o from Order o where o.tradingcodeMGD = :tradingcodeMGD and o.user.name = :userName")
  Order findOrderByTradingcodeMGDAndUsername(
            @Param("tradingcodeMGD") String tradingcodeMGD,
            @Param("userName") String userName
    );
    Order findOrdersByTradingcodeMGD(String tradingcodeMGD);
}
