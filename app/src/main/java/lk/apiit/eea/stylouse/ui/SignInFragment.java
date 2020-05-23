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

import java.util.Date;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentSignInBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import lk.apiit.eea.stylouse.utils.Navigator;
import retrofit2.Response;

public class SignInFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentSignInBinding binding;
    private NavController navController;
    private AwesomeValidation validation;

    @Inject
    AuthService authService;
    @Inject
    AuthSession session;

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
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);

        binding.btnSignIn.setOnClickListener(this::onSignInClick);
        binding.btnSignUp.setOnClickListener(this::onSignUpClick);
        binding.btnForgotPassword.setOnClickListener(this::onForgotPasswordClick);

        validation.addValidation(binding.username, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));
        validation.addValidation(binding.password, getString(R.string.password_length), getString(R.string.error_password));
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onSignInClick(View view) {
        if (validation.validate()) {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();

            if (error.getValue() != null) error.setValue(null);
            loading.setValue(true);
            SignInRequest signInRequest = new SignInRequest(username, password);
            authService.login(signInRequest, loginCallback);
        }
    }

    private void onSignUpClick(View view) {
        navController.navigate(R.id.action_signInFragment_to_signUpFragment);
    }

    private void onForgotPasswordClick(View view) {
        navController.navigate(R.id.action_signInFragment_to_forgotPasswordFragment);
    }

    private ApiResponseCallback loginCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            SignInResponse body = (SignInResponse) response.body();
            if (body != null) {
                ((ActivityHandler)activity).create(body.getTokenValidation());
                Date expiresAt = new Date(new Date().getTime() + body.getTokenValidation());
                body.setExpiresAt(expiresAt);
                session.setAuthState(body);
                Navigator.navigate(navController, R.id.action_signInFragment_to_mainFragment, null);
            }
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };
}
