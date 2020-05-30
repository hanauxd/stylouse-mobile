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
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.ReviewAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentReviewBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.Review;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.models.responses.ReviewResponse;
import lk.apiit.eea.stylouse.services.ReviewService;
import lk.apiit.eea.stylouse.utils.Navigator;
import retrofit2.Response;

public class ReviewFragment extends HomeBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private MutableLiveData<ReviewResponse> reviewData = new MutableLiveData<>(null);
    private FragmentReviewBinding binding;

    @Inject
    ReviewService reviewService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnRetry.setOnClickListener(this::onRetryClick);
        binding.btnReview.setOnClickListener(this::onReviewClick);
        binding.btnInquiry.setOnClickListener(this::onInquiryClick);
        boolean authenticated = session.getAuthState() != null;
        binding.setIsAuth(authenticated);
        if (authenticated) {
            binding.setUserRole(session.getAuthState().getUserRole());
        }
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        reviewData.observe(getViewLifecycleOwner(), this::onReviewDataChange);
    }

    private void onReviewDataChange(ReviewResponse reviewResponse) {
        if (reviewResponse != null) {
            bindValuesToView(reviewResponse);
            renderRateAverageFragment(reviewResponse);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchReviews();
    }

    private void onRetryClick(View view) {
        fetchReviews();
    }

    private void onReviewClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("product", new Gson().toJson(product()));
        Navigator.navigate(parentNavController, R.id.action_productFragment_to_rateFragment, bundle);
    }

    private void onInquiryClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("product", new Gson().toJson(product()));
        Navigator.navigate(parentNavController, R.id.action_productFragment_to_inquiryFragment, bundle);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void fetchReviews() {
        loading.setValue(true);
        if (error.getValue() != null) error.setValue(null);
        String jwt = session.getAuthState() != null ? session.getAuthState().getJwt() : "";
        reviewService.getReviews(getReviewsCallback, product().getId(), jwt);
    }

    private void bindValuesToView(ReviewResponse reviewResponse) {
        binding.setHasUserRated(reviewResponse.isHasUserRated());
        binding.setCount(reviewResponse.getReviews().size());
        ReviewAdapter adapter = new ReviewAdapter(reviewResponse.getReviews(), removeClickListener());
        binding.reviewList.setAdapter(adapter);
    }

    private void renderRateAverageFragment(ReviewResponse reviewResponse) {
        Bundle bundle = new Bundle();
        bundle.putString("review", new Gson().toJson(reviewResponse));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.rate_average_fragment, RateAverageFragment.class, bundle);
        transaction.commit();
    }

    private ProductResponse product() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        return new Gson().fromJson(productJSON, ProductResponse.class);
    }

    private AdapterItemClickListener removeClickListener() {
        return (session.getAuthState() != null)
                && session.getAuthState().getUserRole().equals("ROLE_ADMIN")
                ? this::removeReview
                : null;
    }

    private void removeReview(String reviewJSON) {
        loading.setValue(true);
        Review review = new Gson().fromJson(reviewJSON, Review.class);
        reviewService.deleteReview(deleteCallback, review.getId(), session.getAuthState().getJwt());
    }

    private ApiResponseCallback getReviewsCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            ReviewResponse reviewResponse = (ReviewResponse) response.body();
            if (reviewResponse != null) {
                reviewData.setValue(reviewResponse);
            }
            error.setValue(null);
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };

    private ApiResponseCallback deleteCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            ReviewResponse reviewResponse = (ReviewResponse) response.body();
            reviewData.setValue(reviewResponse);
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            DynamicToast.makeError(activity, message).show();
        }
    };
}
