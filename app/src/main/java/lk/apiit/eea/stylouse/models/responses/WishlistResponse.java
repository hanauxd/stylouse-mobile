package lk.apiit.eea.stylouse.models.responses;

import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponse {
    private String id;
    private ProductResponse product;
    private SignUpRequest user;
}
