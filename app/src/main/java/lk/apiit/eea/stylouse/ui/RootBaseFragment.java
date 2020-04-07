package lk.apiit.eea.stylouse.ui;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import lk.apiit.eea.stylouse.R;

public class RootBaseFragment extends Fragment {
    Activity activity;
    NavController parentNavController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        this.parentNavController = Navigation.findNavController(activity.findViewById(R.id.root_nav_host));
    }
}
