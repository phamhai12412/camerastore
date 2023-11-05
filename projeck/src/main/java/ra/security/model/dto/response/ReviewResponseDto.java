package ra.security.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long id; // ID của review
    private String comment; // Nội dung review
    private int rating; // Điểm đánh giá
    private Long productId;
    private String productname; // name của sản phẩm được đánh giá
    private String username; // name của người dùng đăng review
}
