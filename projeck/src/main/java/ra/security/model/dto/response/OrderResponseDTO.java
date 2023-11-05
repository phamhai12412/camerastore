package ra.security.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.security.model.domain.CartItem;
import ra.security.model.domain.OrderStatus;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private String username;
    private String phone;
    private String address;
    private String note;
    private List<CartItemResponseDTO> cartItems;
    private Date orderDate;
    private OrderStatus status;
    private double totalAmount;

    public OrderResponseDTO(Long id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }
}
