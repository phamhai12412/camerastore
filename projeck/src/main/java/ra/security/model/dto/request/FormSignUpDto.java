package ra.security.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormSignUpDto {
    @NotNull(message = "name không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(min = 15, message = "Email phải có ít nhất 15 ký tự")
    private String email;
    @NotNull(message = "name không được để trống")
    @Size(min = 6, message = "name phải có ít nhất 6 ký tự")
    private String name;
    @NotNull(message = "Username không được để trống")
    @Size(min = 6, message = "Username phải có ít nhất 6 ký tự")
    private String username;
    @NotNull(message = "password không được để trống")
    @Size(min = 6, message = "password phải có ít nhất 6 ký tự")
    private String password;
    @NotNull(message = "address không được để trống")
    @Size(min = 6, message = "address phải có ít nhất 6 ký tự")
    private String address;
    @NotBlank(message = "Phone không được để trống")
    @Length(min = 10, max = 11, message = "Phone phải có từ 10-11 chữ số")
    @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Phone không hợp lệ")
    private String phone;
}
