package ra.security.model.dto.request.promotionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class
PromotionCategoryRequestDTO {
    @Min(value = 0, message = "Giá trị khuyến mãi nằm trong khoảng 0-100")
    @Max(value = 100, message ="Giá trị khuyến mãi nằm trong khoảng 1-100" )
    private int discountValue;
}
