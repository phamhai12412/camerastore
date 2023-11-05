package ra.security.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    @NotNull(message = "name không được để trống")
    @Size(min = 6, message = "name phải có ít nhất 6 ký tự")
    private String name;
}