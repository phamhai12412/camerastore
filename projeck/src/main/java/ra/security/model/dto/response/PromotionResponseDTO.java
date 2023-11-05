package ra.security.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponseDTO {
    private Long id;
    private String promotionCode;
    private int discountValue;
    private Date startDate;
    private Date endDate;
    private int quantity;

    // Getter và Setter
}
