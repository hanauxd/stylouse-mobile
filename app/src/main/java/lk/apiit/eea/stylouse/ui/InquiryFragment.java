package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentInquiryBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.requests.InquiryRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.InquiryService;
import lk.apiit.eea.stylouse.utils.Navigator;
import retrofit2.Response;

public class InquiryFragment extends RootBaseFragment {
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentInquiryBinding binding;
    private Inquiry inquiry;

    @Inject
    AuthSession session;
    @Inject
    InquiryService inquiryService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInquiryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnSend.setOnClickListener(this::onSendClick);
        fetchInquiryByUserAndProduct();
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onSendClick(View view) {
        if (error.getValue() != null) error.setValue(null);
        String inquiryText = binding.inquiry.getText().toString();
        if (TextUtils.isEmpty(inquiryText)) {
            error.setValue("Inquiry is required");
        } else {
            binding.btnSend.startAnimation();
            InquiryRequest inquiry = new InquiryRequest(product().getId(), inquiryText);
            inquiryService.createInquiry(createCallback, session.getAuthState().getJwt(), inquiry);
        }
    }

    private ProductResponse product() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        return new Gson().fromJson(productJSON, ProductResponse.class);
    }

    private void fetchInquiryByUserAndProduct() {
        inquiryService.getInquiryByUserAndProduct(inquiryCallback, session.getAuthState().getJwt(), product().getId());
    }

    private ApiResponseCallback createCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            DynamicToast.makeSuccess(activity, "Inquiry submitted").show();
            Bundle bundle = new Bundle();
            bundle.putString("product", new Gson().toJson(product()));
            Navigator.navigate(parentNavController, R.id.action_inquiryFragment_to_productFragment, bundle);
        }

        @Override
        public void onFailure(String message) {
            binding.btnSend.revertAnimation();
            error.setValue(message);
        }
    };

    private ApiResponseCallback inquiryCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            inquiry = (Inquiry) response.body();
        }

        @Override
        public void onFailure(String message) {
            error.setValue(message);
        }
    };
}
