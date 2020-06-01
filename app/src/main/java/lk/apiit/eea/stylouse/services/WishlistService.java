package lk.apiit.eea.stylouse.services;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.apis.WishlistAPI;
import lk.apiit.eea.stylouse.models.responses.WishlistResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

public class WishlistService {
    private WishlistAPI wishlistAPI;

    @Inject
    public WishlistService(Retrofit retrofit) {
        this.wishlistAPI = retrofit.create(WishlistAPI.class);
    }

    public void addWishlist(HashMap<String, String> wishlistRequest, ApiResponseCallback callback, String jwt) {
        Call<List<WishlistResponse>> wishlistCall = wishlistAPI.addWishlist(formatToken(jwt), wishlistRequest);
        wishlistCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void deleteWishlist(String id, ApiResponseCallback callback, String jwt) {
        Call<List<WishlistResponse>> wishlistCall = wishlistAPI.deleteWishlist(formatToken(jwt), id);
        wishlistCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void getWishlist(ApiResponseCallback callback, String jwt) {
        Call<List<WishlistResponse>> wishlistCall = wishlistAPI.getWishlist(formatToken(jwt));
        wishlistCall.enqueue(new RetroFitCallback<>(callback));
    }
}
