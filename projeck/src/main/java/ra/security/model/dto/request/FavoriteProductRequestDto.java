package ra.security.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteProductRequestDto {
    @NotNull(message = "userId không được để trống")
    @Min(value = 0, message = "Id không hợp lệ")
    private Long userId;
    @NotNull(message = "productId không được để trống")
    @Min(value = 0, message = "Id không hợp lệ")
    private Long productId;
}
