package lk.apiit.eea.stylouse.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShippingRequest {
    String address;
    String city;
    String postalCode;
    String paymentMethod;
}
