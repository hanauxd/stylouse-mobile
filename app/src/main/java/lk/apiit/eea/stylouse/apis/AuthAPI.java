package lk.apiit.eea.stylouse.apis;

import java.util.Map;

import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("login")
    Call<SignInResponse> login(@Body SignInRequest signInRequest);

    @POST("register")
    Call<SignInResponse> register(@Body SignUpRequest signUpRequest);

    @FormUrlEncoded
    @POST("/reset-password-request")
    Call<Map<String, String>> resetPasswordRequest(@Field("email") String email);

    @POST("/reset-password-confirmation")
    Call<SignInResponse> resetPasswordConfirmation(@Body SignInRequest confirmationRequest);

    @POST("/reset-password")
    Call<SignInResponse> resetPassword(@Header("Authorization") String token, @Body SignInRequest resetPasswordRequest);
}
