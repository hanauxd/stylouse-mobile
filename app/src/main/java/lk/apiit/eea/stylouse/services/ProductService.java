package lk.apiit.eea.stylouse.services;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.ProductAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.requests.ProductRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

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

    public void updateProduct(ApiResponseCallback callback, String jwt, ProductResponse product) {
        Call<ProductResponse> updateCall = productAPI.updateProduct(formatToken(jwt), product.getId(), product);
        updateCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void createProduct(ApiResponseCallback callback, String jwt, ProductRequest product, List<MultipartBody.Part> file) {
        Call<ProductResponse> createCall = productAPI.createProduct(formatToken(jwt), product, file);
        createCall.enqueue(new RetroFitCallback<>(callback));
    }
}
