package ra.security.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormUpdateUserDto {
    @Size(min = 6, message = "Name phải có ít nhất 6 ký tự")
    private String name;
    @Email(message = "Email không đúng định dạng")
    @Size(min = 15, message = "Email phải có ít nhất 15 ký tự")
    private String email;
    @Size(min = 6, message = "Address phải có ít nhất 6 ký tự")
    private String address;
    @Length(min = 10, max = 11, message = "Phone phải có từ 10-11 chữ số")
    @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Phone không hợp lệ")
    private String phone;
}
