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
public class ReviewRequestDto {
    private String comment;
    @Min(value = 1, message = "rating đánh giá từ 1-10")
    @Max(value = 10, message = "rating đánh giá từ 1-10")
    private int rating;
    @NotNull(message = "productId không được để trống")
    @Min(value = 0, message = "Id không hợp lệ")
    private Long productId;
}
