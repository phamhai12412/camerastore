package ra.security.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {
    @NotNull(message = "typeDelivery không được để trống")
    private String typeDelivery;

}
