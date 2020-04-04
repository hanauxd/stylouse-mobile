package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import retrofit2.Response;

public class SignInFragment extends RootBaseFragment implements View.OnClickListener, ApiResponseCallback {

    private static final String TAG = "SignInFragment";
    private FragmentSignInBinding binding;
    private NavController navController;
    private AwesomeValidation validation;
    private ProgressBar progressBar;

    @Inject
    AuthService authService;
    @Inject
    AuthSession session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StylouseApp applicationInstance = (StylouseApp) activity.getApplicationContext();
        applicationInstance.getAppComponent().inject(this);

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

        binding.btnSignIn.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);

        validation.addValidation(binding.username, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));
        validation.addValidation(binding.password, getString(R.string.password_length), getString(R.string.error_password));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in: {
                onSignInClick();
                break;
            }
            case R.id.btn_sign_up: {
                navController.navigate(R.id.action_signInFragment_to_signUpFragment);
                break;
            }
        }
    }

    private void onSignInClick() {
        if (validation.validate()) {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();

            SignInRequest signInRequest = new SignInRequest(username, password);
            authService.login(signInRequest, this);

            binding.layoutSpinner.setVisibility(View.VISIBLE);
            binding.layoutSignInForm.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(Response<?> response) {
        SignInResponse body = (SignInResponse) response.body();
        if (body != null) {
            ((ActivityHandler)activity).create(body.getTokenValidation());
            Date expiresAt = new Date(new Date().getTime() + body.getTokenValidation());
            body.setExpiresAt(expiresAt);
            session.setAuthState(body);
            navController.navigate(R.id.action_signInFragment_to_mainFragment);
        }
    }

    @Override
    public void onFailure(String message) {
        displayMessage(message);
        binding.layoutSpinner.setVisibility(View.GONE);
        binding.layoutSignInForm.setVisibility(View.VISIBLE);
    }

    private void displayMessage(String message) {
        binding.errorMessage.setVisibility(View.VISIBLE);
        binding.errorMessage.setText(message);
    }
}
