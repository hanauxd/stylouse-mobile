package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lk.apiit.eea.stylouse.R;

public class SignOutFragment extends RootBaseFragment implements Runnable{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(this, 2000);
    }

    @Override
    public void run() {
        parentNavController.navigate(R.id.action_signOutFragment_to_signInFragment);
    }
}
