package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;

import java.util.Date;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentResetPasswordBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Response;

public class ResetPasswordFragment extends RootBaseFragment {
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentResetPasswordBinding binding;
    private NavController navController;

    @Inject
    AuthService authService;
    @Inject
    AuthSession session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnResetPassword.setOnClickListener(this::onResetPasswordClick);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onResetPasswordClick(View view) {
        String password = binding.password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            error.setValue("Password is required");
        } else {
            if (error != null) error.setValue(null);
            binding.btnResetPassword.startAnimation();
            SignInResponse authToken = new Gson().fromJson(authToken(), SignInResponse.class);
            SignInRequest authRequest = new SignInRequest(authToken.getUserId(), password);
            authService.resetPassword(authRequest, authToken.getJwt(), resetPasswordCallback);
        }
    }

    private ApiResponseCallback resetPasswordCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            SignInResponse body = (SignInResponse) response.body();
            if (body != null) {
                ((ActivityHandler) activity).create(body.getTokenValidation());
                Date expiresAt = new Date(new Date().getTime() + body.getTokenValidation());
                body.setExpiresAt(expiresAt);
                session.setAuthState(body);

                if (navController.getGraph().getId() == R.id.home_nav_graph) {
                    navController.navigate(R.id.action_resetPasswordFragment_to_navigation_profile);
                } else {
                    navController.navigate(R.id.action_resetPasswordFragment_to_mainFragment);
                }
            }
        }

        @Override
        public void onFailure(String message) {
            binding.btnResetPassword.revertAnimation();
            error.setValue(message);
        }
    };

    private String authToken() {
        return getArguments() != null ? getArguments().getString("authToken") : null;
    }
}
