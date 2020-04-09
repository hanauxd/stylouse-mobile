package lk.apiit.eea.stylouse.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemResponse {
    private String id;
    private ProductResponse product;
    private int quantity;
    private String size;
}
