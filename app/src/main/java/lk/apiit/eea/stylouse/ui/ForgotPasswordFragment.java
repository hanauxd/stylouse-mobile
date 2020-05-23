package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentForgotPasswordBinding;
import lk.apiit.eea.stylouse.services.AuthService;
import lk.apiit.eea.stylouse.utils.Navigator;
import retrofit2.Response;

public class ForgotPasswordFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentForgotPasswordBinding binding;
    private AwesomeValidation validation;
    private NavController navController;

    @Inject
    AuthService authService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplicationContext()).getAppComponent().inject(this);
        validation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        validation.setContext(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnSend.setOnClickListener(this::onSendClick);
        validation.addValidation(binding.email, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onSendClick(View view) {
        if (validation.validate()) {
            if (error.getValue() != null) error.setValue(null);
            loading.setValue(true);
            String email = binding.email.getText().toString();
            binding.btnSend.startAnimation();
            authService.resetPasswordRequest(email, resetCallback);
        }
    }

    private ApiResponseCallback resetCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            loading.setValue(false);
            Bundle bundle = new Bundle();
            bundle.putString("email", binding.email.getText().toString());
            Navigator.navigate(navController, R.id.action_forgotPasswordFragment_to_otpFragment, bundle);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            binding.btnSend.revertAnimation();
            error.setValue(message);
        }
    };
}
