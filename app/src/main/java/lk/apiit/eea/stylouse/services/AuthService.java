package lk.apiit.eea.stylouse.services;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.AuthAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

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
        Call<ResponseBody> authCall = authAPI.register(signUpRequest);
        authCall.enqueue(new RetroFitCallback<>(callback));
    }
}
