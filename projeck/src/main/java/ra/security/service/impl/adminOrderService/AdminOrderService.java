package ra.security.service.impl.adminOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.security.exception.CartItemException;
import ra.security.exception.OrderException;
import ra.security.model.domain.*;
import ra.security.model.dto.request.DeliveryRequest;
import ra.security.model.dto.response.*;
import ra.security.repository.OrderRepository;

import ra.security.repository.ProductRepository;
import ra.security.service.IAdminOrderService;
import ra.security.service.impl.usersevice.MailService;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminOrderService implements IAdminOrderService {
    @Autowired
    MailService mailService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<OrderHistoryResponseDTO> getOrderHistory(OrderStatus orderStatus) throws CartItemException {
        List<Order> orders = orderRepository.findAllByStatus(orderStatus);
       return orders.stream().map(order -> orderconvertLS(order)).collect(Collectors.toList());
    }
    @Override
    public OrderHistoryResponseDTO getOrderById(Long id) throws OrderException{
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null&&!order.getStatus().equals(OrderStatus.CART)) {
            return orderconvertLS(order);
        }
      throw new OrderException("Đơn hàng không tồn tại");
    }
    public OrderStatus findDeliveryByInput(String typeDelivery) throws OrderException {
        switch (typeDelivery.toUpperCase().trim()) {
            case "PENDING":
                return OrderStatus.PENDING;  // Đang chờ xác nhận
            case "FAILURE":
                return OrderStatus.FAILURE; // Thất bại
            case "CANCELED":
                return OrderStatus.CANCELED;  // Đã hủy
            case "CONFIRMED":
                return OrderStatus.CONFIRMED; // Đã xác nhận
            case "SHIPPED":
                return OrderStatus.SHIPPED; // Đang giao hàng
            case "DELIVERED":
                return OrderStatus.DELIVERED; // Đã nhận hàng
            default:
                throw new OrderException("Kiểm tra lại trạng thái giao hàng muốn cập nhật");
        }
    }

    @Override
    public OrderHistoryResponseDTO updateOrderStatus(DeliveryRequest typeDelivery, Long id) throws OrderException {
        Order orders = orderRepository.findById(id).orElseThrow(() -> new OrderException("Đơn hàng không tồn tại"));
        if(orders.getStatus().equals(OrderStatus.CART)){
        throw new OrderException("Đơn hàng không tồn tại");
        }
        OrderStatus orderStatus = orders.getStatus();

        OrderStatus newStatus = findDeliveryByInput(typeDelivery.getTypeDelivery());

        switch (orderStatus) {
            case PENDING:
                if (newStatus == OrderStatus.CONFIRMED) {
                    orders.setStatus(newStatus);
                    sendConfirmationEmail(orders);
                } else {
                    throw new OrderException("Bạn đang ở trạng thái chờ xác nhận, không thể chuyển sang trạng thái này");
                }
                break;

            case CONFIRMED:
                if (newStatus == OrderStatus.SHIPPED) {
                    orders.setStatus(newStatus);
                    sendConfirmationEmail(orders);
                } else {
                    throw new OrderException("Bạn đang ở trạng thái đã xác nhận, không thể chuyển sang trạng thái này");
                }
                break;

            case SHIPPED:
                if (newStatus == OrderStatus.DELIVERED) {
                    orders.setStatus(newStatus);
                    sendConfirmationEmail(orders);
                } else
                if(newStatus==OrderStatus.FAILURE){
                    List<CartItem> cartItemList=orders.getCartItems();
                    for (CartItem c:cartItemList
                    ) {
                        Product product=productRepository.findById(c.getProduct().getId()).orElse(null);
                        if(product!=null){
                            product.setQuantity(product.getQuantity()+c.getQuantity());
                            productRepository.save(product);
                        }
                    }
                    orders.setStatus(newStatus);
                    sendConfirmationEmail(orders);
                }else
                {
                    throw new OrderException("Bạn đang ở trạng thái đang giao hàng, không thể chuyển sang trạng thái này");
                }
                break;

            case CANCELED:
            case FAILURE:
            case DELIVERED:
                throw new OrderException("Đơn hàng đã bị hủy, thất bại hoặc đã nhận hàng, không thể thay đổi trạng thái.");

            default:
                throw new OrderException("Trạng thái đơn hàng không hợp lệ");
        }


        Order updatedOrder = orderRepository.save(orders);
        return orderconvertLS(updatedOrder);
    }

    private void sendConfirmationEmail(Order order) {
        mailService.sendEmail(order.getUser().getEmail(), "Thông báo đơn hàng: ", " Xin chào: " + order.getUser().getName() + " đơn hàng của bạn đang ở trạng thái: "+ order.getStatus());
    }





    @Override

    public Double getTotalRevenueForCurrentMonth() {
        return orderRepository.getTotalRevenueForCurrentMonth();
    }


    @Override

    public Double getTotalRevenueForCurrentYear() {
        return orderRepository.getTotalRevenueForCurrentYear();
    }

    @Override

    public Page<ProductResponseDTO> getTop5SellingProducts() {
        Pageable pageable = PageRequest.of(0, 5); // Lấy 5 sản phẩm đầu tiên
        Page<Product> topSellingProducts = orderRepository.findTop5SellingProducts(OrderStatus.DELIVERED, pageable);

        // Chuyển đổi từ Page<Product> sang Page<ProductResponseDTO>
        Page<ProductResponseDTO> topSellingProductsDTO = topSellingProducts.map(this::convertToResponseDTO);

        return topSellingProductsDTO;
    }
    @Override
    public Page<ProductResponseDTO> getTop5ReviewedProducts() {
        Pageable pageable = PageRequest.of(0, 5); // Lấy 5 sản phẩm đầu tiên
        Page<Product> topReviewedProducts = orderRepository.getTop5ReviewedProducts(pageable);

        // Chuyển đổi từ Page<Product> sang Page<ProductResponseDTO>
        Page<ProductResponseDTO> topReviewedProductsDTO = topReviewedProducts.map(this::convertToResponseDTO);

        return topReviewedProductsDTO;
    }

    @Override

    public Page<ProductResponseDTO> getTop5FavoriteProducts() {
        Pageable pageable = PageRequest.of(0, 5); // Lấy 5 sản phẩm đầu tiên
        Page<Product> topFavoriteProducts = orderRepository.getTop5FavoriteProducts(pageable);

        // Chuyển đổi từ Page<Product> sang Page<ProductResponseDTO>
        Page<ProductResponseDTO> topFavoriteProductsDTO = topFavoriteProducts.map(this::convertToResponseDTO);

        return topFavoriteProductsDTO;
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
                .quantity(product.getQuantity())
                .imgurl(product.getImgurl())
                .discountevent(product.getDiscountevent())
                .categoryname(product.getCategory() != null ? product.getCategory().getName() : null)
                .manufacturername(product.getManufacturer() != null ? product.getManufacturer().getName() : null)
                .productImgList(productImgList)
                .build();
    }

    private OrderHistoryResponseDTO orderconvertLS(Order order) {
        List<CartItemResponseDTOLS> cartItemResponseDTOList = order.getCartItems().stream()
                .map(this::cartItemconverLS)
                .collect(Collectors.toList());
        return OrderHistoryResponseDTO.builder()
                .id(order.getId())
                .username(order.getUser().getUsername())
                .phone(order.getUser().getPhone())
                .address(order.getUser().getAddress())
                .orderDate(order.getOrderDate())
                .cartItems(cartItemResponseDTOList)
                .eventcode(order.getEventcode())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().toString())
                .build();
    }

    private CartItemResponseDTOLS cartItemconverLS(CartItem cartItem) {
        double priceevent = cartItem.getPrice() * (1 - ((double) cartItem.getDiscount() / 100));

        return new CartItemResponseDTOLS(
                cartItem.getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getImgurl(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartItem.getDiscount(),
                priceevent,
                priceevent * cartItem.getQuantity()
        );
    }
    @Override
    public OrderHistoryResponseDTO findOrderByTradingcodeMGD(String mgd) throws OrderException {

        Order order = orderRepository.findOrdersByTradingcodeMGD(mgd);
        if(order==null){
            throw new OrderException("Không tìm thấy đơn hàng");
        }

        if (order.getStatus().equals(OrderStatus.CART)) {
            throw new OrderException("Không tìm thấy đơn hàng");
        }
        return orderconvertLS(order);
    }
}
