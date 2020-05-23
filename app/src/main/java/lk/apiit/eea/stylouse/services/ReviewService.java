package lk.apiit.eea.stylouse.services;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.apis.ReviewAPI;
import lk.apiit.eea.stylouse.models.requests.ReviewRequest;
import lk.apiit.eea.stylouse.models.responses.ReviewResponse;
import lk.apiit.eea.stylouse.utils.StringFormatter;
import retrofit2.Call;
import retrofit2.Retrofit;

public class ReviewService {
    private ReviewAPI reviewAPI;

    @Inject
    public ReviewService(Retrofit retrofit) {
        this.reviewAPI = retrofit.create(ReviewAPI.class);
    }

    public void getReviews(ApiResponseCallback callback, String productId, String jwt) {
        String token = "".equals(jwt) ? "" : StringFormatter.formatToken(jwt);
        Call<ReviewResponse> reviewsCall = reviewAPI.getReviews(productId, token);
        reviewsCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void createReview(ApiResponseCallback callback, ReviewRequest request, String jwt) {
        Call<ReviewResponse> reviewsCall = reviewAPI.createReview(request, StringFormatter.formatToken(jwt));
        reviewsCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void deleteReview(ApiResponseCallback callback, String reviewId, String jwt) {
        Call<ReviewResponse> reviewsCall = reviewAPI.deleteReview(reviewId, StringFormatter.formatToken(jwt));
        reviewsCall.enqueue(new RetroFitCallback<>(callback));
    }
}
