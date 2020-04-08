package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.requests.CartRequest;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartAPI {
    @POST("cart")
    Call<List<CartResponse>> addCart(@Body CartRequest cartRequest, @Header("Authorization") String token);

    @GET("cart")
    Call<List<CartResponse>> getCarts(@Header("Authorization") String token);

    @DELETE("cart/{id}")
    Call<List<CartResponse>> deleteCart(@Header("Authorization") String token, @Path("id") String id);
}
