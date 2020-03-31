package lk.apiit.eea.stylouse.apis;

import com.google.gson.Gson;

import lk.apiit.eea.stylouse.models.responses.ErrorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroFitCallback<T> implements Callback<T> {
    private ApiResponseCallback callback;

    public RetroFitCallback(ApiResponseCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful() && response.errorBody() != null) {
            String errorMessage = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class).getMessage();
            callback.onFailure(errorMessage);
        }

        if (response.body() != null) {
            callback.onSuccess(response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        callback.onFailure("Network error occurred. Please try again later.");
    }
}
