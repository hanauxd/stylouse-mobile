package lk.apiit.eea.stylouse.models.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
    private String id;
    private String name;
    private int quantity;
    private double price;
    private String description;
    private List<ProductCategoryResponse> productCategories = new ArrayList<>();
    private List<ProductImageResponse> productImages = new ArrayList<>();

    public ProductResponse(String id, String name, int quantity, double price, String description) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }
}
