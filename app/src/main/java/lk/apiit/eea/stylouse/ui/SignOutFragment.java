package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.databinding.FragmentSignOutBinding;

import static lk.apiit.eea.stylouse.databinding.FragmentSignOutBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class SignOutFragment extends RootBaseFragment implements Runnable{

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignOutBinding binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(this, Long.parseLong(getString(R.string.delay)));
    }

    @Override
    public void run() {
        navigate(parentNavController, R.id.action_signOutFragment_to_mainFragment, null);
    }
}
