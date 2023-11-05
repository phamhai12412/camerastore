package ra.security.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryResponseDTO {
    private Long id;
    private String username;
    private String address;
    private String phone;
    private List<CartItemResponseDTOLS> cartItems;
    private Date orderDate;
    private String status;
    private int eventcode;
    private double totalAmount;
    private String tradingcodeMGD;

    // Constructors, getters, and setters
}
