package lk.apiit.eea.stylouse.services;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.CartAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.requests.CartRequest;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

public class CartService {
    private CartAPI cartAPI;

    @Inject
    public CartService(Retrofit retrofit) {
        this.cartAPI = retrofit.create(CartAPI.class);
    }

    public void addCart(CartRequest cartRequest, ApiResponseCallback callback, String token) {
        Call<List<CartResponse>> cartCall = cartAPI.addCart(cartRequest, "Bearer ".concat(token));
        cartCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void getCarts(ApiResponseCallback callback, String token) {
        Call<List<CartResponse>> cartsCall = cartAPI.getCarts("Bearer ".concat(token));
        cartsCall.enqueue(new RetroFitCallback<>(callback));
    }
}
