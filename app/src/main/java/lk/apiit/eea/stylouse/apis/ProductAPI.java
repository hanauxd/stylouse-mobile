package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("products?sort=createdAt,desc")
    Call<List<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") String id);
}
