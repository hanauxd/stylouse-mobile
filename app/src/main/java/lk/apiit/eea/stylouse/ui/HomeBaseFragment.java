package lk.apiit.eea.stylouse.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.di.UserStore;

public class HomeBaseFragment extends Fragment {

    protected Activity activity;
    protected NavController parentNavController;

    @Inject
    UserStore userStore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        parentNavController = Navigation.findNavController(activity.findViewById(R.id.root_nav_host));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StylouseApp applicationInstance = (StylouseApp) activity.getApplicationContext();
        applicationInstance.getAppComponent().inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(userStore.getUserDetails(activity) == null) {
            parentNavController.navigate(R.id.signInFragment);
        }
    }
}
