package lk.apiit.eea.stylouse.services;

import java.util.Map;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.AuthAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

public class AuthService {
    private AuthAPI authAPI;

    @Inject
    public AuthService(Retrofit retrofit) {
        this.authAPI = retrofit.create(AuthAPI.class);
    }

    public void login(SignInRequest signInRequest, ApiResponseCallback callback) {
        Call<SignInResponse> authCall = authAPI.login(signInRequest);
        authCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void register(SignUpRequest signUpRequest, ApiResponseCallback callback) {
        Call<SignInResponse> authCall = authAPI.register(signUpRequest);
        authCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void resetPasswordRequest(String email, ApiResponseCallback callback) {
        Call<Map<String, String>> resetPasswordRequestCall = authAPI.resetPasswordRequest(email);
        resetPasswordRequestCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void resetPasswordConfirmation(SignInRequest confirmationRequest, ApiResponseCallback callback) {
        Call<SignInResponse> confirmationCall = authAPI.resetPasswordConfirmation(confirmationRequest);
        confirmationCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void resetPassword(SignInRequest resetPasswordRequest, String jwt, ApiResponseCallback callback) {
        Call<SignInResponse> resetPasswordCall = authAPI.resetPassword(formatToken(jwt), resetPasswordRequest);
        resetPasswordCall.enqueue(new RetroFitCallback<>(callback));
    }
}
