package ra.security.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDTO {
    @NotNull(message = "productId không được để trống")
    @Min(value = 0, message = "Id không hợp lệ")
    private Long productId;
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private int quantity;
}