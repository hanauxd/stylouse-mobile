package lk.apiit.eea.stylouse.apis;

import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserAPI {
    @GET("users")
    Call<SignUpRequest> getUser(@Header("Authorization") String token);
}
