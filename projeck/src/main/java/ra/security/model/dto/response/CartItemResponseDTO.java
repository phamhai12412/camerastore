package ra.security.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private Long id;
    @JsonIgnoreProperties({"quantity","categoryId","categoryname","manufacturerId","manufacturername","productImgList","status"})
    private ProductResponseDTO product;
    private int quantity;
    private double price;
    private int discount;
    private double PriceDiscount;
    private double intototalmoney;
}