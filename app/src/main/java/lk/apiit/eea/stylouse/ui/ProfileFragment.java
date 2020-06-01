package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.google.gson.Gson;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentProfileBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.UserService;
import retrofit2.Response;

import static androidx.navigation.Navigation.findNavController;
import static lk.apiit.eea.stylouse.databinding.FragmentProfileBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Constants.ROLE_ADMIN;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class ProfileFragment extends AuthFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private NavController navController;
    private FragmentProfileBinding binding;

    @Inject
    AuthSession session;
    @Inject
    UserService userService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
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
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Profile");
        navController = findNavController(view);
        binding.btnRetry.setOnClickListener(this::fetchUser);

        if (session.getAuthState() != null) {
            binding.btnLogout.setOnClickListener(this::onLogoutClick);
            binding.btnChangePassword.setOnClickListener(this::onChangePasswordClick);
            loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
            error.observe(getViewLifecycleOwner(), this::onErrorChange);
            fetchUser(view);
        }
    }

    private void onChangePasswordClick(View view) {
        SignInResponse authState = session.getAuthState();
        String authJSON = new Gson().toJson(authState);
        Bundle bundle = new Bundle();
        bundle.putString("authToken", authJSON);
        if (authState.getUserRole().equals(ROLE_ADMIN)) {
            navigate(parentNavController, R.id.action_adminFragment_to_resetPasswordFragment, bundle);
        } else {
            navigate(navController, R.id.action_navigation_profile_to_resetPasswordFragment, bundle);
        }
    }

    private void fetchUser(View view) {
        if (error.getValue() != null) error.setValue(null);
        loading.setValue(true);
        userService.getUser(userCallback, session.getAuthState().getJwt());
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean aBoolean) {
        binding.setLoading(aBoolean);
    }

    private void onLogoutClick(View view) {
        ((ActivityHandler) activity).destroy();
        session.setAuthState(null);
    }

    private ApiResponseCallback userCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            SignUpRequest user = (SignUpRequest) response.body();
            binding.setUser(user);
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };
}
