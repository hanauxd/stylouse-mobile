package lk.apiit.eea.stylouse.services;

import java.util.List;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.OrderAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

public class OrderService {
    private OrderAPI orderAPI;

    public OrderService(Retrofit retrofit) {
        this.orderAPI = retrofit.create(OrderAPI.class);
    }

    public void getOrder(ApiResponseCallback callback, String jwt) {
        Call<List<OrdersResponse>> ordersCall = orderAPI.getOrders(formatToken(jwt));
        ordersCall.enqueue(new RetroFitCallback<>(callback));
    }
}
