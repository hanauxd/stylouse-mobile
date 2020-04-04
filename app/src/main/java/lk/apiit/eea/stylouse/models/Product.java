package lk.apiit.eea.stylouse.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    private String id;
    private String name;
    private int quantity;
    private double price;
    private String description;
    private List<ProductCategory> productCategories = new ArrayList<>();
    private List<ProductImage> productImages = new ArrayList<>();
}
