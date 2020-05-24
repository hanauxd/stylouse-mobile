package lk.apiit.eea.stylouse.models;

import java.util.List;

import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inquiry {
    private String id;
    private SignUpRequest user;
    private ProductResponse product;
    private List<Reply> replies;
}
