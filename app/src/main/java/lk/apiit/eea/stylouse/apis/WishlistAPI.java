package lk.apiit.eea.stylouse.apis;

import java.util.HashMap;
import java.util.List;

import lk.apiit.eea.stylouse.models.responses.WishlistResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WishlistAPI {
    @POST("wishlist")
    Call<List<WishlistResponse>> addWishlist(@Header("Authorization") String token,
                                             @Body HashMap<String, String> wishlistRequest);

    @DELETE("wishlist/{id}")
    Call<List<WishlistResponse>> deleteWishlist(@Header("Authorization") String token,
                                                @Path("id") String id);

    @GET("wishlist")
    Call<List<WishlistResponse>> getWishlist(@Header("Authorization") String token);
}
