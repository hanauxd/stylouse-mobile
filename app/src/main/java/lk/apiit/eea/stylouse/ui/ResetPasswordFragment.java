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
import lk.apiit.eea.stylouse.databinding.FragmentResetPasswordBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static androidx.navigation.Navigation.findNavController;
import static lk.apiit.eea.stylouse.databinding.FragmentResetPasswordBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = findNavController(view);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnResetPassword.setOnClickListener(this::onResetPasswordClick);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onResetPasswordClick(View view) {
        String password = binding.password.getText().toString();
        if (isEmpty(password)) {
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
            SignInResponse authToken = (SignInResponse) response.body();
            if (authToken != null) {
                ((ActivityHandler) activity).create(authToken.getTokenValidation());
                session.save(authToken);
                if (navController.getGraph().getId() == R.id.home_nav_graph) {
                    navigate(navController, R.id.action_resetPasswordFragment_to_navigation_profile, null);
                } else {
                    navigate(navController, R.id.action_resetPasswordFragment_to_mainFragment, null);
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
