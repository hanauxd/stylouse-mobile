package lk.apiit.eea.stylouse.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartResponse {
    private String id;
    private ProductResponse product;
    private int quantity;
    private String size;
    private double totalPrice;

    public CartResponse(ProductResponse product, int quantity, String size, double totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.size = size;
        this.totalPrice = totalPrice;
    }
}
