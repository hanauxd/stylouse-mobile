package lk.apiit.eea.stylouse.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentSignUpBinding;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Response;

public class SignUpFragment extends RootBaseFragment implements View.OnClickListener, ApiResponseCallback {

    private FragmentSignUpBinding binding;
    private NavController navController;
    private AwesomeValidation validation;

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

        validation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        validation.setContext(activity);
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

        addFormValidation();
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
        binding.layoutSpinner.setVisibility(View.GONE);
        binding.layoutSignUpForm.setVisibility(View.VISIBLE);
    }

    private void onSignUpClick() {
        if (validation.validate()) {
            String role = "ROLE_USER";
            String firstName = binding.firstName.getText().toString();
            String lastName = binding.lastName.getText().toString();
            String phone = binding.phone.getText().toString();
            String email = binding.username.getText().toString();
            String password = binding.password.getText().toString();

            SignUpRequest signUpRequest = new SignUpRequest(role, firstName, lastName, phone, email, password);
            authService.register(signUpRequest, this);

            binding.layoutSpinner.setVisibility(View.VISIBLE);
            binding.layoutSignUpForm.setVisibility(View.GONE);

        }
    }

    private void displayMessage(String message) {
        binding.errorMessage.setText(message);
        binding.errorMessage.setVisibility(View.VISIBLE);
    }

    private void addFormValidation() {
        validation.addValidation(binding.firstName, RegexTemplate.NOT_EMPTY, getString(R.string.error_field));
        validation.addValidation(binding.lastName, RegexTemplate.NOT_EMPTY, getString(R.string.error_field));
        validation.addValidation(binding.phone, getString(R.string.regex_phone), getString(R.string.error_phone));
        validation.addValidation(binding.username, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));
        validation.addValidation(binding.password, getString(R.string.password_length), getString(R.string.error_password));
    }
}
