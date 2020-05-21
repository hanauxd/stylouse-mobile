package lk.apiit.eea.stylouse.apis;

import retrofit2.Response;

public interface ApiResponseCallback {
    void onSuccess(Response<?> response);
    void onFailure(String message);
}
