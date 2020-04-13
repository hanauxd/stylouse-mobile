package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        }

        @Override
        public void onFailure(String message) {
            DynamicToast.makeError(activity, message).show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
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

        if (session.getAuthState() != null) {
            userService.getUser(userCallback, session.getAuthState().getJwt());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        activity.getMenuInflater().inflate(R.menu.profile_items_auth, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_orders: {
                navController.navigate(R.id.action_navigation_profile_to_ordersFragment);
                break;
            }
            case R.id.action_logout:{
                ((ActivityHandler)activity).destroy();
                session.setAuthState(null);
            }
            default: {}
        }
        return super.onOptionsItemSelected(item);
    }
}
