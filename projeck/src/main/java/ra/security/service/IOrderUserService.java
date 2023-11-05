package ra.security.service;

import org.springframework.data.domain.Pageable;
import ra.security.exception.CartItemException;
import ra.security.exception.OrderException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.CartItem;
import ra.security.model.domain.Order;
import ra.security.model.domain.OrderStatus;
import ra.security.model.dto.request.CartItemRequestDTO;
import ra.security.model.dto.request.promotionDto.PromotionCodeRequestDTO;
import ra.security.model.dto.response.CartItemResponseDTO;
import ra.security.model.dto.response.OrderHistoryResponseDTO;
import ra.security.model.dto.response.OrderResponseDTO;
import ra.security.model.dto.response.ProductResponseDTO;

import java.util.List;

public interface IOrderUserService {
    public OrderHistoryResponseDTO findOrderByTradingcodeMGDAndUsername(String mgd,String username) throws OrderException;
    List<OrderHistoryResponseDTO> getlistoderuse(String username) throws CartItemException;
    List<ProductResponseDTO> getProductsuse(String name, Pageable pageable);
    OrderHistoryResponseDTO placeOrder(String username, PromotionCodeRequestDTO promotionCodeRequestDTO) throws CartItemException, PromotionException;


    OrderHistoryResponseDTO getOrderById(Long id,String username) throws OrderException;

    List<OrderHistoryResponseDTO> getOrderHistory(OrderStatus orderStatus, String username) throws CartItemException;
    List<CartItemResponseDTO> addCartItem(CartItemRequestDTO cartItem, String usename) throws CartItemException;
    List<CartItemResponseDTO> deleteCartItem(Long cartItemId, String username) throws CartItemException;
    OrderHistoryResponseDTO cancelOrder(String username, Long id) throws CartItemException, OrderException;
    void clearCart(String username) throws CartItemException;
    List<CartItemResponseDTO> getAllCartItems(String username) throws CartItemException;
    List<CartItemResponseDTO> editCartItem(Long cartItemId,CartItemRequestDTO cartItemRequestDTO, String username) throws CartItemException;
}
