package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentSignInBinding;
import lk.apiit.eea.stylouse.di.UserStore;
import lk.apiit.eea.stylouse.models.requests.SignInRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Response;

public class SignInFragment extends RootBaseFragment implements View.OnClickListener, ApiResponseCallback {

    private static final String TAG = "SignInFragment";
    private FragmentSignInBinding binding;
    private NavController navController;

    @Inject
    AuthService authService;
    @Inject
    UserStore userStore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StylouseApp applicationInstance = (StylouseApp) getActivity().getApplicationContext();
        applicationInstance.getAppComponent().inject(this);
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
        String username = binding.username.getText().toString();
        String password = binding.password.getText().toString();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            SignInRequest signInRequest = new SignInRequest(username, password);
            authService.login(signInRequest, this);
        } else {
            displayMessage("Username and Password cannot be empty.");
        }
    }

    @Override
    public void onSuccess(Response<?> response) {
        SignInResponse signInResponse = (SignInResponse) response.body();
        userStore.putUserDetails(signInResponse, activity);
        navController.navigate(R.id.mainFragment);
    }

    @Override
    public void onFailure(String message) {
        displayMessage(message);
    }

    private void displayMessage(String message) {
        binding.errorMessage.setVisibility(View.VISIBLE);
        binding.errorMessage.setText(message);
    }
}
