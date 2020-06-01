package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.google.gson.Gson;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentOtpBinding;
import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static androidx.navigation.Navigation.findNavController;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeSuccess;
import static lk.apiit.eea.stylouse.databinding.FragmentOtpBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class OtpFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentOtpBinding binding;
    private NavController navController;

    @Inject
    AuthService authService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplicationContext()).getAppComponent().inject(this);
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
        navController = findNavController(view);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.setEmail(email());
        binding.btnValidateOtp.setOnClickListener(this::onValidateClick);
        binding.btnResendOtp.setOnClickListener(this::onResendClick);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onValidateClick(View view) {
        String OTP = binding.otp.getText().toString();
        if (isEmpty(OTP)) {
            error.setValue("OTP is required");
        } else {
            binding.btnValidateOtp.startAnimation();
            binding.btnResendOtp.setEnabled(false);
            SignInRequest confirmationRequest = new SignInRequest(email(), OTP);
            authService.resetPasswordConfirmation(confirmationRequest, validationCallback);
        }
    }

    private void onResendClick(View view) {
        binding.btnResendOtp.startAnimation();
        binding.btnValidateOtp.setEnabled(false);
        authService.resetPasswordRequest(email(), resendCallback);
    }

    private ApiResponseCallback validationCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            SignInResponse authToken = (SignInResponse) response.body();
            String authJSON = new Gson().toJson(authToken);
            Bundle bundle = new Bundle();
            bundle.putString("authToken", authJSON);
            navigate(navController, R.id.action_otpFragment_to_resetPasswordFragment, bundle);
        }

        @Override
        public void onFailure(String message) {
            binding.btnValidateOtp.revertAnimation();
            binding.btnResendOtp.setEnabled(true);
            error.setValue(message);
        }
    };

    private ApiResponseCallback resendCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            binding.btnResendOtp.revertAnimation();
            binding.btnValidateOtp.setEnabled(true);
            makeSuccess(activity, "A new OTP has been sent to your email.").show();
        }

        @Override
        public void onFailure(String message) {
            binding.btnResendOtp.revertAnimation();
            binding.btnValidateOtp.setEnabled(true);
            error.setValue(message);
        }
    };

    private String email() {
        return getArguments() != null ? getArguments().getString("email") : null;
    }
}
