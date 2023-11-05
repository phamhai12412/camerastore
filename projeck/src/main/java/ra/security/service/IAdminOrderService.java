package ra.security.service;

import ra.security.exception.CartItemException;
import ra.security.exception.OrderException;
import ra.security.model.domain.Order;
import ra.security.model.domain.OrderStatus;
import ra.security.model.dto.request.DeliveryRequest;
import ra.security.model.dto.response.OrderHistoryResponseDTO;
import ra.security.model.dto.response.OrderResponseDTO;
import ra.security.model.dto.response.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAdminOrderService {
    OrderHistoryResponseDTO findOrderByTradingcodeMGD(String mgd) throws OrderException;
    List<OrderHistoryResponseDTO> getOrderHistory(OrderStatus orderStatus) throws CartItemException;

    OrderHistoryResponseDTO getOrderById(Long id) throws OrderException;

    OrderHistoryResponseDTO updateOrderStatus(DeliveryRequest typeDelivery, Long id) throws OrderException;

    Double getTotalRevenueForCurrentMonth();

    Double getTotalRevenueForCurrentYear();

    Page<ProductResponseDTO> getTop5SellingProducts();

    Page<ProductResponseDTO> getTop5ReviewedProducts();

    Page<ProductResponseDTO> getTop5FavoriteProducts();

}
