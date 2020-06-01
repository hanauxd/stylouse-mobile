package lk.apiit.eea.stylouse.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class SignInResponse {
    @SerializedName("userId")
    private final String userId;
    @SerializedName("tokenValidation")
    private final long tokenValidation;
    @SerializedName("jwt")
    private final String jwt;
    @SerializedName("userRole")
    private final String userRole;
    private Date expiresAt;

    public void setExpireDate() {
        Date expiresAt = new Date(new Date().getTime() + this.tokenValidation);
        setExpiresAt(expiresAt);
    }
}
