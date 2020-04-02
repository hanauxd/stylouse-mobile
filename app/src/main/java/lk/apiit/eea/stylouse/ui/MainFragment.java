package lk.apiit.eea.stylouse.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.databinding.FragmentMainBinding;

public class MainFragment extends RootBaseFragment {

    private FragmentMainBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = binding.navigation;
        NavController navController = Navigation.findNavController(activity, R.id.home_nav_host);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public void onPause() {
        super.onPause();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}
