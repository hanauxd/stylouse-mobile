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
import lk.apiit.eea.stylouse.databinding.FragmentAdminBinding;

public class AdminFragment extends RootBaseFragment {
    private FragmentAdminBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = binding.adminNavigation;
        NavController navController = Navigation.findNavController(activity, R.id.admin_nav_host);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}