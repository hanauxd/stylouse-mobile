package lk.apiit.eea.stylouse.ui;

import android.content.Context;
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
import lk.apiit.eea.stylouse.databinding.FragmentSignUpBinding;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Response;

public class SignUpFragment extends RootBaseFragment implements View.OnClickListener, ApiResponseCallback {

    private static final String TAG = "SignUpFragment";
    private FragmentSignUpBinding binding;
    private NavController navController;

    @Inject
    AuthService authService;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StylouseApp app = (StylouseApp) activity.getApplicationContext();
        app.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up: {
                onSignUpClick();
                break;
            }
            case R.id.btn_sign_in: {
                navController.navigate(R.id.action_signUpFragment_to_signInFragment);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Response<?> response) {
        navController.navigate(R.id.action_signUpFragment_to_signInFragment);
    }

    @Override
    public void onFailure(String message) {
        displayMessage(message);
    }

    private void onSignUpClick() {
        String firstName = binding.firstName.getText().toString();
        String lastName = binding.lastName.getText().toString();
        String phone = binding.phone.getText().toString();
        String email = binding.username.getText().toString();
        String password = binding.password.getText().toString();

        if (
                TextUtils.isEmpty(firstName)
                        || TextUtils.isEmpty(lastName)
                        || TextUtils.isEmpty(phone)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(password)
        ) {
            displayMessage("Fields cannot be empty.");
        } else {
            String role = "ROLE_USER";
            SignUpRequest signUpRequest = new SignUpRequest(role, firstName, lastName, phone, email, password);
            authService.register(signUpRequest, this);
        }
    }

    private void displayMessage(String message) {
        binding.errorMessage.setText(message);
        binding.errorMessage.setVisibility(View.VISIBLE);
    }
}
