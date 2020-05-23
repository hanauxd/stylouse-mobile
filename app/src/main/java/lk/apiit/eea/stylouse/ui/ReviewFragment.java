package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.ReviewAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentReviewBinding;
import lk.apiit.eea.stylouse.models.Review;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.models.responses.ReviewResponse;
import lk.apiit.eea.stylouse.services.ReviewService;
import lk.apiit.eea.stylouse.utils.Navigator;
import retrofit2.Response;

public class ReviewFragment extends HomeBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentReviewBinding binding;

    @Inject
    ReviewService reviewService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnRetry.setOnClickListener(this::fetchReviews);
        binding.btnReview.setOnClickListener(this::onReviewClick);
        binding.setIsAuth(session.getAuthState() != null);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        fetchReviews(view);
    }

    private void onReviewClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("product", new Gson().toJson(product()));
        Navigator.navigate(parentNavController, R.id.action_productFragment_to_rateFragment, bundle);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void fetchReviews(View view) {
        loading.setValue(true);
        if (error.getValue() != null) error.setValue(null);
        String jwt = session.getAuthState() != null ? session.getAuthState().getJwt() : "";
        reviewService.getReviews(getReviewsCallback, product().getId(), jwt);
    }

    private ApiResponseCallback getReviewsCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            ReviewResponse reviewResponse = (ReviewResponse) response.body();
            if (reviewResponse != null) {
                initRecyclerView(reviewResponse.getReviews());
                binding.setHasUserRated(reviewResponse.isHasUserRated());
                binding.setCount(reviewResponse.getReviews().size());
            }
            error.setValue(null);
            loading.setValue(false);
            renderRateAverageFragment(reviewResponse);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };

    private void initRecyclerView(List<Review> reviews) {
        ReviewAdapter adapter = new ReviewAdapter(reviews);
        binding.reviewList.setAdapter(adapter);
    }

    private ProductResponse product() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        return new Gson().fromJson(productJSON, ProductResponse.class);
    }

    private void renderRateAverageFragment(ReviewResponse reviewResponse) {
        Bundle bundle = new Bundle();
        bundle.putString("review", new Gson().toJson(reviewResponse));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.rate_average_fragment, RateAverageFragment.class, bundle);
        transaction.commit();
    }
}
