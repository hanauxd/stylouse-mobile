package lk.apiit.eea.stylouse.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String id;
    private String role;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;

    public SignUpRequest(String role, String firstName, String lastName, String phone, String email, String password) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
}
