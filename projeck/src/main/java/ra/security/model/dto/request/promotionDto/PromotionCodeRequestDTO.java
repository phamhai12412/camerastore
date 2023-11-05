package ra.security.model.dto.request.promotionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionCodeRequestDTO {
    @NotNull(message = "promotionCode không được để trống")
    @Size(min = 6, message = "promotionCode phải có ít nhất 6 ký tự")
    private String promotionCode;
    @Min(value = 1, message = "Giá trị khuyến mãi nằm trong khoảng 1-100")
    @Max(value = 100, message ="Giá trị khuyến mãi nằm trong khoảng 1-100" )
    private int discountValue;
    @NotNull(message = "startDate không được để trống")
    private Date startDate;
    @NotNull(message = "endDate không được để trống")
    private Date endDate;


}
