package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("products?sort=createdAt,desc")
    Call<List<ProductResponse>> getProducts();

    @GET("products/{id}")
    Call<ProductResponse> getProduct(@Path("id") String id);

    @POST("products/{id}")
    Call<ProductResponse> updateProduct(@Header("Authorization") String token, @Path("id") String id, @Body ProductResponse product);
}
