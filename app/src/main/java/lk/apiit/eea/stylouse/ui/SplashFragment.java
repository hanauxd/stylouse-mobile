package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;

public class SplashFragment extends RootBaseFragment {
    @Inject
    AuthSession session;
    @Nullable
    @Inject
    SignInResponse state;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp)activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
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
                ((ActivityHandler)activity).create(difference);
            } else {
                session.setAuthState(null);
            }
        }
        parentNavController.navigate(R.id.action_splashFragment_to_mainFragment);
    }
}
