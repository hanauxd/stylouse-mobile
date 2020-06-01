package lk.apiit.eea.stylouse.ui;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import lk.apiit.eea.stylouse.R;

import static androidx.navigation.Navigation.findNavController;

public class RootBaseFragment extends Fragment {
    Activity activity;
    NavController parentNavController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        this.parentNavController = findNavController(activity.findViewById(R.id.root_nav_host));
    }
}
