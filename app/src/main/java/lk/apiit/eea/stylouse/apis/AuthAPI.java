package lk.apiit.eea.stylouse.apis;

import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/login")
    Call<SignInResponse> login(@Body SignInRequest signInRequest);

    @POST("/register")
    Call<ResponseBody> register(@Body SignUpRequest signUpRequest);
}
