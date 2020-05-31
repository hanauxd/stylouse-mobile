package lk.apiit.eea.stylouse.models.requests;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private int quantity;
    private double price;
    private String description;
    private List<String> categories = new ArrayList<>();
}
