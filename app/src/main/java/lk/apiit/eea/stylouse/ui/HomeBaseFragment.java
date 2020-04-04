package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;

public class HomeBaseFragment extends RootBaseFragment {
    @Nullable
    @Inject
    SignInResponse state;
    @Inject
    AuthSession session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (state != null) {
            session.setObserver(this, appState -> {
                if (appState == null) {
                    parentNavController.navigate(R.id.action_mainFragment_to_signOutFragment);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        session.removeObserver(this);
    }
}
