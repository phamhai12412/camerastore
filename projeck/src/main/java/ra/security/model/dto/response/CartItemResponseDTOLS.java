package ra.security.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTOLS {
    private Long id;
    private String productName;
    private String productImageUrl;
    private int quantity;
    private double price;
    private double discount;
    private double PriceDiscount;
    private double intototalmoney;

}
