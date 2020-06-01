package lk.apiit.eea.stylouse.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentSignUpBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Response;

import static androidx.navigation.Navigation.findNavController;
import static lk.apiit.eea.stylouse.databinding.FragmentSignUpBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Constants.ROLE_USER;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class SignUpFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentSignUpBinding binding;
    private NavController navController;
    private AwesomeValidation validation;

    @Inject
    AuthService authService;
    @Inject
    AuthSession session;

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
        binding.btnSignIn.setOnClickListener(this::onSignInClick);
        binding.btnSignUp.setOnClickListener(this::onSignUpClick);
        validateInput();
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onSignUpClick(View view) {
        if (validation.validate()) {
            String firstName = binding.firstName.getText().toString();
            String lastName = binding.lastName.getText().toString();
            String phone = binding.phone.getText().toString();
            String email = binding.username.getText().toString();
            String password = binding.password.getText().toString();

            if (error.getValue() != null) error.setValue(null);
            loading.setValue(true);
            SignUpRequest signUpRequest = new SignUpRequest(ROLE_USER, firstName, lastName, phone, email, password);
            authService.register(signUpRequest, registerCallback);
        }
    }

    private void onSignInClick(View view) {
        navigate(navController, R.id.action_signUpFragment_to_signInFragment, null);
    }

    private void validateInput() {
        validation.addValidation(binding.firstName, RegexTemplate.NOT_EMPTY, getString(R.string.error_field));
        validation.addValidation(binding.lastName, RegexTemplate.NOT_EMPTY, getString(R.string.error_field));
        validation.addValidation(binding.phone, getString(R.string.regex_phone), getString(R.string.error_phone));
        validation.addValidation(binding.username, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));
        validation.addValidation(binding.password, getString(R.string.password_length), getString(R.string.error_password));
    }

    private ApiResponseCallback registerCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            SignInResponse authToken = (SignInResponse) response.body();
            if (authToken != null) {
                ((ActivityHandler) activity).create(authToken.getTokenValidation());
                session.save(authToken);
                navigate(navController, R.id.action_signUpFragment_to_mainFragment, null);
            }
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };
}
