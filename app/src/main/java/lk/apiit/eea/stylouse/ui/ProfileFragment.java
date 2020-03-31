package lk.apiit.eea.stylouse.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentProfileBinding;
import lk.apiit.eea.stylouse.di.UserStore;

public class ProfileFragment extends HomeBaseFragment implements View.OnClickListener {

    @Inject
    UserStore userStore;

    private Activity activity;
    private NavController parentNavController;
    private NavController navController;
    private FragmentProfileBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        parentNavController = Navigation.findNavController(activity.findViewById(R.id.root_nav_host));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StylouseApp applicationInstance = (StylouseApp) activity.getApplicationContext();
        applicationInstance.getAppComponent().inject(this);
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
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        int resourceId = userStore.getUserDetails(activity) ? R.menu.profile_items_auth : R.menu.profile_items_unauth;
        activity.getMenuInflater().inflate(resourceId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_orders: {
                navController.navigate(R.id.action_navigation_profile_to_ordersFragment);
                break;
            }
            case R.id.action_logout:
            case R.id.action_sign_in: {
                parentNavController.navigate(R.id.signInFragment);
                break;
            }
            default: {}
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
