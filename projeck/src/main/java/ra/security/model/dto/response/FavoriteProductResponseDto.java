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
public class FavoriteProductResponseDto {
    private Long id;
    private String username;
    private Long productID;
    private String productname;
    private Date favoriteTime;

    // Các getter và setter
}
