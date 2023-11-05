package ra.security.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    @NotNull(message = "name không được để trống")
    @Size(min = 6, message = "name phải có ít nhất 6 ký tự")
    private String name;
    @NotNull(message = "description không được để trống")
    @Size(min = 6, message = "description phải có ít nhất 6 ký tự")
    private String description;
    @NotNull(message = "price không được để trống")
    @Min(value = 1, message = "price phải lớn hơn 0")
    private double price;
    @NotNull(message = "quantity không được để trống")
    @Min(value = 1, message = "quantity phải lớn hơn 0")
    private int quantity;
    @NotNull(message = "categoryId không được để trống")
    @Min(value = 0, message = "categoryId không hợp lệ")
    private Long categoryId;
    @NotNull(message = "manufacturerId không được để trống")
    @Min(value = 0, message = "manufacturerId không hợp lệ")
    private Long manufacturerId;
    private List<MultipartFile> file;
}
