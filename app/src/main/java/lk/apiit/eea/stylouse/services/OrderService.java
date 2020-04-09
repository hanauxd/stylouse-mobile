package lk.apiit.eea.stylouse.services;

import java.util.List;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.OrderAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

public class OrderService {
    private OrderAPI orderAPI;

    public OrderService(Retrofit retrofit) {
        this.orderAPI = retrofit.create(OrderAPI.class);
    }

    public void getOrder(ApiResponseCallback callback, String jwt) {
        Call<List<OrdersResponse>> ordersCall = orderAPI.getOrders(token(jwt));
        ordersCall.enqueue(new RetroFitCallback<>(callback));
    }

    private String token(String jwt) {
        return "Bearer ".concat(jwt);
    }

}
