package lk.apiit.eea.stylouse.models.responses;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class SignInResponse {
    @SerializedName("userId")
    private final String userId;
    @SerializedName("tokenValidation")
    private final String tokenValidation;
    @SerializedName("jwt")
    private final String jwt;
    @SerializedName("userRole")
    private final String userRole;
}
