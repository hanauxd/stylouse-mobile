package lk.apiit.eea.stylouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private String id;
    private String category;

    public Category(String category) {
        this.category = category;
    }
}
