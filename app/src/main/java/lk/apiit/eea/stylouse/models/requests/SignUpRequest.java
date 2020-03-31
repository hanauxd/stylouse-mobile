package lk.apiit.eea.stylouse.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
}
