package lk.apiit.eea.stylouse.models.responses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.apiit.eea.stylouse.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private List<Review> reviews = new ArrayList<>();
    private Map<String, Integer> count = new HashMap<>();
    private boolean hasUserRated;
    private double average;
}
