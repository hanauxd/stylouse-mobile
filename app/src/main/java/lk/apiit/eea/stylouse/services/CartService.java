package lk.apiit.eea.stylouse.services;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.CartAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.requests.CartRequest;
import lk.apiit.eea.stylouse.models.requests.ShippingRequest;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

public class CartService {
    private CartAPI cartAPI;

    @Inject
    public CartService(Retrofit retrofit) {
        this.cartAPI = retrofit.create(CartAPI.class);
    }

    public void addCart(CartRequest cartRequest, ApiResponseCallback callback, String jwt) {
        Call<List<CartResponse>> cartCall = cartAPI.addCart(formatToken(jwt), cartRequest);
        cartCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void getCarts(ApiResponseCallback callback, String jwt) {
        Call<List<CartResponse>> cartsCall = cartAPI.getCarts(formatToken(jwt));
        cartsCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void deleteCart(ApiResponseCallback callback, String jwt, String id) {
        Call<List<CartResponse>> cartsCall = cartAPI.deleteCart(formatToken(jwt), id);
        cartsCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void checkout(ApiResponseCallback callback, ShippingRequest shippingRequest, String jwt) {
        Call<OrdersResponse> checkoutCall = cartAPI.checkout(formatToken(jwt), shippingRequest);
        checkoutCall.enqueue(new RetroFitCallback<>(callback));
    }
}
