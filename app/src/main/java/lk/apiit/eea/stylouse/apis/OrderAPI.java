package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.responses.OrdersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface OrderAPI {
    @GET("orders?sort=date,desc")
    Call<List<OrdersResponse>> getOrders(@Header("Authorization") String token);
}
