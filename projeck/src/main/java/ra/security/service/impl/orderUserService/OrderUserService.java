package ra.security.service.impl.orderUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.security.exception.CartItemException;
import ra.security.exception.OrderException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.*;
import ra.security.model.dto.request.CartItemRequestDTO;
import ra.security.model.dto.request.promotionDto.PromotionCodeRequestDTO;
import ra.security.model.dto.response.*;
import ra.security.repository.*;
import ra.security.service.IOrderUserService;
import ra.security.service.impl.promotionService.PromotionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderUserService implements IOrderUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Override
    public List<OrderHistoryResponseDTO> getlistoderuse(String username) {
        return orderRepository.listOrderByUsernameAndStatusNotCart(username).stream().map(order -> orderconvertLS(order)).collect(Collectors.toList());
    }
    @Override
    public List<ProductResponseDTO> getProductsuse(String name, Pageable pageable) {
        Page<Product> products;
        if (name.isEmpty()) {
            products = productRepository.findAllByStatus(true,pageable);
        } else {
            products = productRepository.findAllByNameContainingIgnoreCaseAndStatus(name,true, pageable);
        }

        return products.map(this::convertToResponseDTO).getContent();
    }
    @Override
    public OrderHistoryResponseDTO placeOrder(String username, PromotionCodeRequestDTO promotionCodeRequestDTO) throws CartItemException, PromotionException {

        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("không tìm thấy use"));

        } else {
            throw new CartItemException("token xác thực không hợp lệ");
        }
        PromotionResponseDTO promotionResponseDTO = null;
        Promotion promotion=null;
        if (promotionCodeRequestDTO.getPromotionCode() != null) {
            Optional<Promotion> promotionOptional = promotionRepository.findByPromotionCode(promotionCodeRequestDTO.getPromotionCode());

            if (promotionOptional.isPresent()) {
                 promotion = promotionOptional.get();
                Date currentDate = new Date();

                if (promotion.getEndDate().before(currentDate)) {
                    throw new PromotionException("Mã giảm giá đã hết hạn");
                }else if (promotion.getQuantity()<=0){
                    throw new PromotionException("Mã giảm giá đã hết số lần sử dụng");
                }

            } else {
                throw new PromotionException("Mã giảm giá không tồn tại");
            }
        }
        Order order = orderRepository.findByStatusAndUser_Id(OrderStatus.CART, users.getId());

        if(order==null|| order.getCartItems().isEmpty()){
            throw new CartItemException("Giỏ hàng của bạn đang trống");
        }
        double totalAmount = 0;
        for (CartItem c : order.getCartItems()
        ) {
            double priceevent = c.getProduct().getPrice() * (1 - ((double) c.getProduct().getDiscountevent() / 100));
            totalAmount = totalAmount + priceevent * c.getQuantity();
            c.setPrice(c.getProduct().getPrice());
            c.setDiscount(c.getProduct().getDiscountevent());
            Product product=productRepository.findById(c.getProduct().getId()).orElse(null);
            if(product!=null){
                if(product.getQuantity()<c.getQuantity()){
                    throw new CartItemException("sản phẩm:"+product.getName()+" vượt quá số lượng tồn kho, số lượng trong kho còn: "+ product.getQuantity());
                }
                product.setQuantity(product.getQuantity()-c.getQuantity());
                productRepository.save(product);
            }

        }
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());
        if (promotion != null) {
            totalAmount = totalAmount * (1 - ((double) promotion.getDiscountValue() / 100));
            order.setEventcode(promotion.getDiscountValue());
            promotion.getOrder().add(order);
            if(promotion.getQuantity()>0){
                promotion.setQuantity(promotion.getQuantity()-1);
            }

       promotionRepository.save(promotion);

        }
        order.setTotalAmount(totalAmount);
        order.setPromotion(promotion);
        order.setTradingcodeMGD("MVD100001"+order.getId());
        return orderconvertLS(orderRepository.save(order));
    }

    @Override
    public OrderHistoryResponseDTO cancelOrder(String username, Long id) throws CartItemException, OrderException {


        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("không tìm thấy use"));

        } else {
            throw new CartItemException("token xác thực không hợp lệ");
        }
        Order order = orderRepository.findByIdAndUser_Id(id, users.getId());
        if (order == null) {
            throw new OrderException("Đơn hàng không tồn tại");
        }
        if (order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.CONFIRMED)) {
            List<CartItem> cartItemList=order.getCartItems();
            for (CartItem c:cartItemList
                 ) {
                Product product=productRepository.findById(c.getProduct().getId()).orElse(null);
                if(product!=null){
                    product.setQuantity(product.getQuantity()+c.getQuantity());
                    productRepository.save(product);
                }
            }
            order.setStatus(OrderStatus.CANCELED);
            order = orderRepository.save(order);
        } else {
            throw new OrderException("Thay đổi trạng thái đơn hàng không thành công, bạn chỉ thay đổi được trạng thái khi đơn hàng đang chờ hoặc mới xác nhận");
        }
        return orderconvertLS(order);
    }

    @Override
    public OrderHistoryResponseDTO getOrderById(Long id,String username) throws OrderException {
//       chú ý phần này là Ls
        // Lấy thông tin đơn hàng theo ID
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderException("Đơn hàng không tồn tại"));
        if (order.getStatus().equals(OrderStatus.CART)) {
            throw new OrderException("Đơn hàng không tồn tại");
        }
        if(!order.getUser().getName().equals(username)){
            throw new OrderException("Đơn hàng không tồn tại");
        }
        return orderconvertLS(order);
    }
    @Override
    public OrderHistoryResponseDTO findOrderByTradingcodeMGDAndUsername(String mgd,String username) throws OrderException {

        Order order = orderRepository.findOrderByTradingcodeMGDAndUsername(mgd,username);
                if(order==null){
                    throw new OrderException("Không tìm thấy đơn hàng");
                }

        if (order.getStatus().equals(OrderStatus.CART)) {
            throw new OrderException("Không tìm thấy đơn hàng");
        }
        return orderconvertLS(order);
    }

    @Override
    public List<OrderHistoryResponseDTO> getOrderHistory(OrderStatus orderStatus, String username) throws CartItemException {
//       chú ý phần này là Ls
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("không tìm thấy use"));

        } else {
            throw new CartItemException("token xác thực không hợp lệ");
        }

        List<Order> orders = orderRepository.findAllByStatusAndUser_Id(orderStatus, users.getId());
        return orders.stream().map(order -> orderconvertLS(order)).collect(Collectors.toList());

    }


    @Override
    public List<CartItemResponseDTO> addCartItem(CartItemRequestDTO cartItem, String username) throws CartItemException {
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("không tìm thấy use"));

        } else {
            throw new CartItemException("token xác thực không hợp lệ");
        }
        Product product=productRepository.findById(cartItem.getProductId()).orElseThrow(()-> new CartItemException("Không tìm thấy sản phẩm"));
if(!product.isStatus()){
    throw new CartItemException("sản phẩm đã hết hàng");
}
        // Lấy danh sách các CartItem dựa trên orderStatus và userId
        List<CartItem> cartItemList = cartItemRepository.findByOrderStatusAndUserId(OrderStatus.CART, users.getId());
        Order order = orderRepository.findByStatusAndUser_Id(OrderStatus.CART, users.getId());
        if (order == null) {
            order = Order.builder().orderDate(new Date())
                    .cartItems(cartItemList)
                    .status(OrderStatus.CART)
                    .user(users)
                    .eventcode(0)
                    .totalAmount(0)
                    .build();
            order = orderRepository.save(order);

        }

        // Kiểm tra xem CartItem đã tồn tại trong danh sách chưa
        boolean check = false;
        CartItem cardcheck = null;
        for (CartItem c : cartItemList) {
            if (c.getProduct().getId().equals(cartItem.getProductId())) {
                c.setQuantity(c.getQuantity() + cartItem.getQuantity());
                cardcheck = c;
                check = true;
                break;
            }
        }


        if (!check) {
            CartItem cartItemnew = CartItem.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .order(order)
                    .discount(product.getDiscountevent())
                    .price(product.getPrice()*(1- (double) (product.getDiscountevent()) /100))
                    .build();

            cartItemList.add(cartItemnew);

//            cartItemRepository.save(cartItemnew);
            order.setCartItems(cartItemList);
            orderRepository.save(order);

        } else {
            cartItemRepository.save(cardcheck);
            order.setCartItems(cartItemList);
            orderRepository.save(order);


        }
        return cartItemList.stream().map(cartItem1 -> cartItemconverGH(cartItem1)).collect(Collectors.toList());

    }

    @Override
    public List<CartItemResponseDTO> deleteCartItem(Long cartItemId, String username) throws CartItemException {
//        chú ý cover GH
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("không tìm thấy use"));

        } else {
            throw new CartItemException("token xác thực không hợp lệ");
        }

        // Lấy danh sách các CartItem dựa trên orderStatus và userId
        List<CartItem> cartItemList = cartItemRepository.findByOrderStatusAndUserId(OrderStatus.CART, users.getId());
        Order order = orderRepository.findByStatusAndUser_Id(OrderStatus.CART, users.getId());
        if (order == null) {
            throw new CartItemException("Giỏ hàng của bạn đang trống");

        }

        // Tìm CartItem cần xóa

        boolean check = false;

        // Sử dụng vòng lặp foreach để tìm và xóa CartItem
        for (CartItem cartItem : cartItemList) {
            if (cartItem.getId().equals(cartItemId)) {
                // Xóa CartItem
                cartItemList.remove(cartItem);
                cartItemRepository.delete(cartItem);
                order.setCartItems(cartItemList);
                orderRepository.save(order);
                check = true;
                break;
            }
        }

        if (!check) {
            throw new CartItemException("CartItem không tồn tại");
        }

        return cartItemList.stream().map(cartItem -> cartItemconverGH(cartItem)).collect(Collectors.toList());
    }

    @Override
    public List<CartItemResponseDTO> editCartItem(Long cartItemId, CartItemRequestDTO cartItemRequestDTO, String username) throws CartItemException {
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("không tìm thấy use"));

        } else {
            throw new CartItemException("token xác thực không hợp lệ");
        }
        if(cartItemRequestDTO.getQuantity()<=0){
            throw new CartItemException("số lượng sản phẩm phải lớn hơn 1");
        }
        // Lấy danh sách các CartItem dựa trên orderStatus và userId
        List<CartItem> cartItemList = cartItemRepository.findByOrderStatusAndUserId(OrderStatus.CART, users.getId());
        Order order = orderRepository.findByStatusAndUser_Id(OrderStatus.CART, users.getId());
        if (order == null) {
            throw new CartItemException("Giỏ hàng của bạn đang trống");
        }

        // Tìm CartItem cần chỉnh sửa
        CartItem cartItemToEdit = null;
        for (CartItem cartItem : cartItemList) {
            if (cartItem.getId().equals(cartItemId)) {
                cartItemToEdit = cartItem;
                break;
            }
        }

        if (cartItemToEdit == null) {
            throw new CartItemException("CartItem không tồn tại");
        }

        // Cập nhật số lượng mới cho CartItem
        cartItemToEdit.setQuantity(cartItemRequestDTO.getQuantity());
        cartItemRepository.save(cartItemToEdit);

        // Cập nhật danh sách CartItem trong order và lưu lại order
        order.setCartItems(cartItemList);
        orderRepository.save(order);

        return cartItemList.stream().map(cartItem -> cartItemconverGH(cartItem)).collect(Collectors.toList());
    }

    @Override
    public void clearCart(String username) throws CartItemException {
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("Không tìm thấy người dùng"));
        } else {
            throw new CartItemException("Token xác thực không hợp lệ");
        }

        // Lấy danh sách các CartItem dựa trên orderStatus và userId
        List<CartItem> cartItemList = cartItemRepository.findByOrderStatusAndUserId(OrderStatus.CART, users.getId());
        Order order = orderRepository.findByStatusAndUser_Id(OrderStatus.CART, users.getId());
        if (order == null) {
            throw new CartItemException("Giỏ hàng của bạn đang trống");
        }

        // Xóa toàn bộ CartItem từ cơ sở dữ liệu
        cartItemRepository.deleteAll(cartItemList);

        // Xóa tất cả CartItem khỏi order
        order.getCartItems().clear();
        orderRepository.save(order);
    }

    @Override
    public List<CartItemResponseDTO> getAllCartItems(String username) throws CartItemException {
//        lấy ra giỏ hàng nhớ chú ý cover giỏ hàng
        Users users = null;
        if (username != null) {
            users = userRepository.findByUsername(username).orElseThrow(() -> new CartItemException("Không tìm thấy người dùng"));
        } else {
            throw new CartItemException("Token xác thực không hợp lệ");
        }

        // Lấy danh sách các CartItem dựa trên orderStatus và userId
        List<CartItem> cartItemList = cartItemRepository.findByOrderStatusAndUserId(OrderStatus.CART, users.getId());

        return cartItemList.stream().map(cartItem -> cartItemconverGH(cartItem)).collect(Collectors.toList());
    }



    private CartItemResponseDTO cartItemconverGH(CartItem cartItem) {
        double priceevent = cartItem.getProduct().getPrice() * (1 - ((double) cartItem.getProduct().getDiscountevent() / 100));

        ProductResponseDTO productResponseDTO = productconvertGH(cartItem.getProduct());

        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .product(productResponseDTO)
                .quantity(cartItem.getQuantity())
                .price(cartItem.getProduct().getPrice())
                .discount(cartItem.getProduct().getDiscountevent())
                .PriceDiscount(priceevent)
                .intototalmoney(priceevent * cartItem.getQuantity())
                .build();
    }

    private ProductResponseDTO productconvertGH(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .priceevent(product.getPrice()*(1- (double) (product.getDiscountevent()) /100))
                .imgurl(product.getImgurl())
                .discountevent(product.getDiscountevent())
                .categoryId(product.getCategory().getId())
                .categoryname(product.getCategory().getName())
                .manufacturerId(product.getManufacturer().getId())
                .manufacturername(product.getManufacturer().getName())
                .status(product.isStatus())
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
                .tradingcodeMGD(order.getTradingcodeMGD())
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

}