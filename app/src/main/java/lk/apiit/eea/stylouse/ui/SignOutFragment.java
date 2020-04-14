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

public class SignOutFragment extends RootBaseFragment implements Runnable{
    private FragmentSignOutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignOutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(this, Long.parseLong(getString(R.string.delay)));
    }

    @Override
    public void run() {
        parentNavController.navigate(R.id.action_signOutFragment_to_signInFragment);
    }
}
