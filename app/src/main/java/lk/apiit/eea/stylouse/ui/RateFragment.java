package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentRateBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.requests.ReviewRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.ReviewService;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeSuccess;
import static lk.apiit.eea.stylouse.databinding.FragmentRateBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class RateFragment extends HomeBaseFragment {
    private MutableLiveData<String> error = new MutableLiveData<>();
    private FragmentRateBinding binding;

    @Inject
    ReviewService reviewService;
    @Inject
    AuthSession authSession;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnSend.setOnClickListener(this::onSendClick);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onSendClick(View view) {
        float rating = binding.rate.getRating();
        String reviewText = binding.review.getText().toString();
        if (isEmpty(reviewText) || rating == 0) {
            error.setValue("Fields cannot be empty");
        } else {
            binding.btnSend.startAnimation();
            ReviewRequest review = new ReviewRequest(product().getId(), reviewText, (int) rating);
            reviewService.createReview(createCallback, review, authSession.getAuthState().getJwt());
        }
    }

    private ProductResponse product() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        return new Gson().fromJson(productJSON, ProductResponse.class);
    }

    private ApiResponseCallback createCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            makeSuccess(activity,"Review submitted").show();
            Bundle bundle = new Bundle();
            bundle.putString("product", new Gson().toJson(product()));
            navigate(parentNavController, R.id.action_rateFragment_to_productFragment, bundle);
        }

        @Override
        public void onFailure(String message) {
            binding.btnSend.revertAnimation();
            error.setValue(message);
        }
    };
}
