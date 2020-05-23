package lk.apiit.eea.stylouse.utils;

import android.os.Bundle;
import android.util.Log;

import androidx.navigation.NavController;

public class Navigator {
    private static final String TAG = "Navigator";
    public static void navigate(NavController navController, int action, Bundle bundle) {
        try {
            navController.navigate(action, bundle);
        } catch (Exception ex) {
            Log.e(TAG, "navigate: ".concat(ex.getMessage()));
        }
    }
}
