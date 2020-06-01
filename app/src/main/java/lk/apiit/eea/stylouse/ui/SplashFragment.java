package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentSplashBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;

import static lk.apiit.eea.stylouse.databinding.FragmentSplashBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Constants.ROLE_ADMIN;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class SplashFragment extends RootBaseFragment {
    @Inject
    AuthSession session;
    @Nullable
    @Inject
    SignInResponse state;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
        ActionBar appBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (appBar != null) {
            appBar.hide();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSplashBinding binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {
        if (state != null) {
            Date current = new Date();
            Date expiration = state.getExpiresAt();

            long difference = expiration.getTime() - current.getTime();

            if (difference > 5000) {
                ((ActivityHandler) activity).create(difference);
            } else {
                session.setAuthState(null);
            }
        }
        new Handler().postDelayed(() -> {
            if (state != null && state.getUserRole().equals(ROLE_ADMIN)) {
                navigate(parentNavController, R.id.action_splashFragment_to_adminFragment, null);
            } else {
                navigate(parentNavController, R.id.action_splashFragment_to_mainFragment, null);
            }
        }, 2000);
    }
}
