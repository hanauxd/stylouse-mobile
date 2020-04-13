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
import androidx.navigation.Navigation;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentProfileBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.requests.SignUpRequest;
import lk.apiit.eea.stylouse.services.UserService;
import retrofit2.Response;

public class ProfileFragment extends AuthFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private NavController navController;
    private FragmentProfileBinding binding;

    @Inject
    AuthSession session;
    @Inject
    UserService userService;

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
            DynamicToast.makeError(activity, message).show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);

        if (session.getAuthState() != null) {
            userService.getUser(userCallback, session.getAuthState().getJwt());
        }

        binding.btnLogout.setOnClickListener(this::onLogoutClick);
        binding.btnOrders.setOnClickListener(this::onOrdersClick);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean aBoolean) {
        binding.setLoading(aBoolean);
    }

    private void onOrdersClick(View view) {
        navController.navigate(R.id.action_navigation_profile_to_ordersFragment);
    }

    private void onLogoutClick(View view) {
        ((ActivityHandler)activity).destroy();
        session.setAuthState(null);
    }
}
