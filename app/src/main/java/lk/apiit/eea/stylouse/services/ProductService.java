package lk.apiit.eea.stylouse.services;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.ProductAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

public class ProductService {
    private ProductAPI productAPI;

    @Inject
    public ProductService(Retrofit retrofit) {
        this.productAPI = retrofit.create(ProductAPI.class);
    }

    public void getProducts(ApiResponseCallback callback) {
        Call<List<ProductResponse>> productsCall = productAPI.getProducts();
        productsCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void getProduct(String id, ApiResponseCallback callback) {
        Call<ProductResponse> productCall = productAPI.getProduct(id);
        productCall.enqueue(new RetroFitCallback<>(callback));
    }
}
