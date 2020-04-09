package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.requests.CartRequest;
import lk.apiit.eea.stylouse.models.requests.ShippingRequest;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartAPI {
    @POST("cart")
    Call<List<CartResponse>> addCart(@Header("Authorization") String token, @Body CartRequest cartRequest);

    @GET("cart")
    Call<List<CartResponse>> getCarts(@Header("Authorization") String token);

    @DELETE("cart/{id}")
    Call<List<CartResponse>> deleteCart(@Header("Authorization") String token, @Path("id") String id);

    @POST("cart/checkout")
    Call<OrdersResponse> checkout(@Header("Authorization") String token, @Body ShippingRequest shippingRequest);
}
