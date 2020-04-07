package lk.apiit.eea.stylouse.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryResponse {
    private String id;
    private CategoryResponse category;
}
