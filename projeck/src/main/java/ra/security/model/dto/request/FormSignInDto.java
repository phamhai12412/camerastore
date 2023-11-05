package ra.security.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormSignInDto {
    @NotNull(message = "Username không được để trống")
    @Size(min = 6, message = "Username phải có ít nhất 6 ký tự")
    private String username;
    @NotNull(message = "password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;
}
