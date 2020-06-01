package lk.apiit.eea.stylouse.services;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.apis.UserAPI;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

public class UserService {
    private UserAPI userAPI;

    public UserService(Retrofit retrofit) {
        this.userAPI = retrofit.create(UserAPI.class);
    }

    public void getUser(ApiResponseCallback callback, String jwt) {
        Call<SignUpRequest> userCall = userAPI.getUser(formatToken(jwt));
        userCall.enqueue(new RetroFitCallback<>(callback));
    }
}
