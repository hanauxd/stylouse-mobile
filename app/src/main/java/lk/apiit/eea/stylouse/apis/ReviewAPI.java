package lk.apiit.eea.stylouse.apis;

import lk.apiit.eea.stylouse.models.requests.ReviewRequest;
import lk.apiit.eea.stylouse.models.responses.ReviewResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewAPI {
    @GET("reviews/product/{productId}")
    Call<ReviewResponse> getReviews(@Path("productId") String productId, @Header("Authorization") String token);

    @POST("reviews")
    Call<ReviewResponse> createReview(@Body ReviewRequest request, @Header("Authorization") String token);

    @DELETE("reviews/{id}")
    Call<ReviewResponse> deleteReview(@Path("id") String id, @Header("Authorization") String token);
}
