package lk.apiit.eea.stylouse.apis;

import retrofit2.Response;

public interface ApiResponseCallback {
    public void onSuccess(Response<?> response);
    public void onFailure(String message);
}
