package lk.apiit.eea.stylouse.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartRequest {
    private String productId;
    private String size;
    private int quantity;
}
