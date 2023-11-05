package ra.security.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imgurl;
    private int discountevent;
    private double priceevent;
    private Long categoryId;
    private String categoryname;
    private Long manufacturerId;
    private String manufacturername;
    private List<ProductImgResponseDTO> productImgList;
    private boolean status;
}
