package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.Category;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CategoryAPI {

    @POST("categories")
    Call<List<Category>> addCategory(@Header("Authorization") String token, @Body Category category);

    @GET("categories")
    Call<List<Category>> getCategories(@Header("Authorization") String token);
}
