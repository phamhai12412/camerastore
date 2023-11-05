package ra.security.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.CartItemException;
import ra.security.exception.OrderException;
import ra.security.model.dto.request.DeliveryRequest;
import ra.security.model.dto.response.OrderHistoryResponseDTO;
import ra.security.model.dto.response.OrderResponseDTO;
import ra.security.model.domain.OrderStatus;
import ra.security.service.IAdminOrderService;
import ra.security.model.dto.response.ProductResponseDTO;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@CrossOrigin("*")
public class AdminOrderController {

    @Autowired
    private IAdminOrderService adminOrderService;

    @GetMapping("/history/pending")
    public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistorypending() throws CartItemException {
        // Lấy lịch sử đơn hàng của người dùng
        List<OrderHistoryResponseDTO> orderHistory = adminOrderService.getOrderHistory(OrderStatus.PENDING);
        return new ResponseEntity<>(orderHistory,HttpStatus.OK);
    }
    @GetMapping("/get-mgd/{tradingcodeMGD}")
    public ResponseEntity<OrderHistoryResponseDTO> findOrderByTradingcodeMGDAndUsername(
            @PathVariable("tradingcodeMGD") String tradingcodeMGD
    ) throws OrderException {
        OrderHistoryResponseDTO orderResponseDTO = adminOrderService.findOrderByTradingcodeMGD(tradingcodeMGD );
        return new ResponseEntity<>(orderResponseDTO,HttpStatus.OK);
    }
    @GetMapping("/history/confirmed")
    public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistoryconfirmed() throws CartItemException {
        // Lấy lịch sử đơn hàng của người dùng
        List<OrderHistoryResponseDTO> orderHistory = adminOrderService.getOrderHistory(OrderStatus.CONFIRMED);
        return new ResponseEntity<>(orderHistory,HttpStatus.OK);
    }

    @GetMapping("/history/shipped")
    public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistoryshipped() throws CartItemException {
        // Lấy lịch sử đơn hàng của người dùng
        List<OrderHistoryResponseDTO> orderHistory = adminOrderService.getOrderHistory(OrderStatus.SHIPPED);
        return new ResponseEntity<>(orderHistory,HttpStatus.OK);
    }

    @GetMapping("/history/delivered")
    public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistorydelivered() throws CartItemException {
        // Lấy lịch sử đơn hàng của người dùng
        List<OrderHistoryResponseDTO> orderHistory = adminOrderService.getOrderHistory(OrderStatus.DELIVERED);
        return new ResponseEntity<>(orderHistory,HttpStatus.OK);
    }

    @GetMapping("/history/canceled")
    public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistorycanceled() throws CartItemException {
        // Lấy lịch sử đơn hàng của người dùng
        List<OrderHistoryResponseDTO> orderHistory = adminOrderService.getOrderHistory(OrderStatus.CANCELED);
        return new ResponseEntity<>(orderHistory,HttpStatus.OK);
    }

    @GetMapping("/history/failure")
    public ResponseEntity<List<OrderHistoryResponseDTO>> getOrderHistoryfailure() throws CartItemException {
        // Lấy lịch sử đơn hàng của người dùng
        List<OrderHistoryResponseDTO> orderHistory = adminOrderService.getOrderHistory(OrderStatus.FAILURE);
        return new ResponseEntity<>(orderHistory,HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<OrderHistoryResponseDTO> getOrderById(@PathVariable Long id) throws OrderException {
        OrderHistoryResponseDTO order = adminOrderService.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<OrderHistoryResponseDTO> updateOrderStatus(@PathVariable Long id, @RequestBody @Valid DeliveryRequest typeDelivery) throws OrderException {
        OrderHistoryResponseDTO updatedOrder = adminOrderService.updateOrderStatus(typeDelivery,id);

            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @GetMapping("/revenue/month")
    public ResponseEntity<Double> getTotalRevenueForCurrentMonth() {
        Double revenue = adminOrderService.getTotalRevenueForCurrentMonth();
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }

    @GetMapping("/revenue/year")
    public ResponseEntity<Double> getTotalRevenueForCurrentYear() {
        Double revenue = adminOrderService.getTotalRevenueForCurrentYear();
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<Page<ProductResponseDTO>> getTop5SellingProducts() {
        Page<ProductResponseDTO> products = adminOrderService.getTop5SellingProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/top-reviewed-products")
    public ResponseEntity<Page<ProductResponseDTO>> getTop5ReviewedProducts() {
        Page<ProductResponseDTO> products = adminOrderService.getTop5ReviewedProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/top-favorite-products")
    public ResponseEntity<Page<ProductResponseDTO>> getTop5FavoriteProducts() {
        Page<ProductResponseDTO> products = adminOrderService.getTop5FavoriteProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
