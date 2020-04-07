package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lk.apiit.eea.stylouse.R;

public class AuthFragment extends HomeBaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (state == null) {
            parentNavController.navigate(R.id.signInFragment);
        }
    }
}
