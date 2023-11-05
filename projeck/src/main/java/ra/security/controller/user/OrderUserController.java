package ra.security.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.CartItemException;
import ra.security.exception.LoginException;
import ra.security.exception.OrderException;
import ra.security.exception.PromotionException;
import ra.security.model.domain.CartItem;
import ra.security.model.domain.OrderStatus;
import ra.security.model.dto.request.CartItemRequestDTO;
import ra.security.model.dto.request.FormSignUpDto;
import ra.security.model.dto.request.FormUpdateUserDto;
import ra.security.model.dto.request.promotionDto.PromotionCodeRequestDTO;
import ra.security.model.dto.response.CartItemResponseDTO;
import ra.security.model.dto.response.OrderHistoryResponseDTO;
import ra.security.model.dto.response.OrderResponseDTO;
import ra.security.model.dto.response.ProductResponseDTO;
import ra.security.service.IOrderUserService;
import ra.security.service.IProductService;
import ra.security.service.IUserService;
import ra.security.service.impl.usersevice.UserServiceOnline;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin("*")
public class OrderUserController {
  @Autowired
  private IUserService userService;
  @Autowired
  private UserServiceOnline userServiceOnline;
  @Autowired
  private IOrderUserService orderUserService;
  @GetMapping("/products")
  public ResponseEntity<List<ProductResponseDTO>> getPaginatedProducts(
          @RequestParam(defaultValue = "") String name,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "2") int size,
          @RequestParam(defaultValue = "name") String sort,
          @RequestParam(defaultValue = "asc") String by) {


    Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(by), sort);
    List<ProductResponseDTO> products = (List<ProductResponseDTO>) orderUserService.getProductsuse(name, pageable);
    return new ResponseEntity<>(products, HttpStatus.OK);

  }
  @PutMapping("/updateUserInfo")
  public ResponseEntity<String> signupedit(@RequestBody @Valid FormUpdateUserDto formSignUpDto) throws LoginException {

    userService.updateUserInfo(userServiceOnline.getCurrentUsername(),formSignUpDto);
    return new ResponseEntity<>("Thay đổi thông tin thành công", HttpStatus.CREATED);

  }

  @PostMapping("/purchase")
  public ResponseEntity<OrderHistoryResponseDTO> placeOrder(@RequestBody PromotionCodeRequestDTO promotionCodeRequestDTO) throws CartItemException, PromotionException {
    // Xử lý việc đặt hàng và lưu vào cơ sở dữ liệu
    OrderHistoryResponseDTO orderResponseDTO = orderUserService.placeOrder(userServiceOnline.getCurrentUsername(), promotionCodeRequestDTO);
    return new ResponseEntity<>(orderResponseDTO,HttpStatus.OK);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<OrderHistoryResponseDTO> getOrderByIdUser(@PathVariable Long id) throws OrderException {
    // Lấy thông tin của đơn hàng theo ID
    OrderHistoryResponseDTO orderResponseDTO = orderUserService.getOrderById(id, userServiceOnline.getCurrentUsername());
   return new ResponseEntity<>(orderResponseDTO,HttpStatus.OK);
  }
  @GetMapping("/get-mgd/{tradingcodeMGD}")
  public ResponseEntity<OrderHistoryResponseDTO> findOrderByTradingcodeMGDAndUsername(
          @PathVariable("tradingcodeMGD") String tradingcodeMGD
  ) throws OrderException {
    OrderHistoryResponseDTO orderResponseDTO = orderUserService.findOrderByTradingcodeMGDAndUsername(tradingcodeMGD , userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderResponseDTO,HttpStatus.OK);
  }
  @GetMapping("/historys")
  public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistory() throws CartItemException {
    // Lấy lịch sử đơn hàng của người dùng
    List<OrderHistoryResponseDTO> orderHistory = orderUserService.getlistoderuse(userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderHistory,HttpStatus.OK);
  }
  @GetMapping("/history/pending")
  public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistorypending() throws CartItemException {
    // Lấy lịch sử đơn hàng của người dùng
    List<OrderHistoryResponseDTO> orderHistory = orderUserService.getOrderHistory(OrderStatus.PENDING, userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderHistory,HttpStatus.OK);
  }

  @GetMapping("/history/confirm")
  public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistoryconfirm() throws CartItemException {
    // Lấy lịch sử đơn hàng của người dùng
    List<OrderHistoryResponseDTO> orderHistory = orderUserService.getOrderHistory(OrderStatus.CONFIRMED, userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderHistory,HttpStatus.OK);
  }

  @GetMapping("/history/shipped")
  public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistoryshipped() throws CartItemException {
    // Lấy lịch sử đơn hàng của người dùng
    List<OrderHistoryResponseDTO> orderHistory = orderUserService.getOrderHistory(OrderStatus.SHIPPED, userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderHistory,HttpStatus.OK);
  }

  @GetMapping("/history/delivered")
  public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistorydelivered() throws CartItemException {
    // Lấy lịch sử đơn hàng của người dùng
    List<OrderHistoryResponseDTO> orderHistory = orderUserService.getOrderHistory(OrderStatus.DELIVERED, userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderHistory,HttpStatus.OK);
  }
  @GetMapping("/history/failure")
  public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistoryfailure() throws CartItemException {
    // Lấy lịch sử đơn hàng của người dùng
    List<OrderHistoryResponseDTO> orderHistory = orderUserService.getOrderHistory(OrderStatus.FAILURE, userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderHistory,HttpStatus.OK);
  }
  @GetMapping("/history/canceled")
  public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistorycanceled() throws CartItemException {
    // Lấy lịch sử đơn hàng của người dùng
    List<OrderHistoryResponseDTO> orderHistory = orderUserService.getOrderHistory(OrderStatus.CANCELED, userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(orderHistory,HttpStatus.OK);
  }

  @PutMapping("/select/canceled/{id}")
  public ResponseEntity<OrderHistoryResponseDTO> getOrdercanceled(@PathVariable Long id) throws CartItemException, OrderException {
    OrderHistoryResponseDTO orderResponseDTO = orderUserService.cancelOrder(userServiceOnline.getCurrentUsername(), id);
    return new ResponseEntity<>(orderResponseDTO,HttpStatus.OK);
  }

  @PostMapping("/add/cartItem")
  public ResponseEntity<List<CartItemResponseDTO>> createCartItem(@RequestBody @Valid CartItemRequestDTO cartItem) throws CartItemException {
    List<CartItemResponseDTO> createdCartItem = orderUserService.addCartItem(cartItem, userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(createdCartItem, HttpStatus.OK);
  }

  @GetMapping("/card")
  public ResponseEntity<List<CartItemResponseDTO>> getAllCartItems() throws CartItemException {
    List<CartItemResponseDTO> createdCartItem = orderUserService.getAllCartItems(userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(createdCartItem, HttpStatus.OK);
  }

  @DeleteMapping("/delete/cardall")
  public ResponseEntity<String> clearCart() throws CartItemException {
    orderUserService.clearCart(userServiceOnline.getCurrentUsername());
    return ResponseEntity.ok("Xóa thành công");
  }
  @PutMapping("/edit/card/{id}")
  public ResponseEntity<List< CartItemResponseDTO>> editCartItem(@RequestBody  CartItemRequestDTO cartItemRequestDTO,@PathVariable Long id) throws CartItemException {
    List< CartItemResponseDTO> cartItemResponseDTO= orderUserService.editCartItem(id,cartItemRequestDTO,userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(cartItemResponseDTO, HttpStatus.OK);
  }
  @DeleteMapping("/delete/carditem/{id}")
 public ResponseEntity<List< CartItemResponseDTO>>  deleteCartItem(@PathVariable Long id) throws CartItemException {
    List< CartItemResponseDTO> cartItemResponseDTO= orderUserService.deleteCartItem(id,userServiceOnline.getCurrentUsername());
    return new ResponseEntity<>(cartItemResponseDTO, HttpStatus.OK);
  }
}