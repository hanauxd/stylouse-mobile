package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.requests.ProductRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("products?sort=createdAt,desc")
    Call<List<ProductResponse>> getProducts();

    @POST("products/{id}")
    Call<ProductResponse> updateProduct(@Header("Authorization") String token,
                                        @Path("id") String id,
                                        @Body ProductResponse product);

    @Multipart
    @POST("products")
    Call<ProductResponse> createProduct(@Header("Authorization") String token,
                                        @Part("product") ProductRequest product,
                                        @Part List<MultipartBody.Part> file);
}
