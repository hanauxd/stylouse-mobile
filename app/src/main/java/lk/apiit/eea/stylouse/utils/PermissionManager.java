package lk.apiit.eea.stylouse.utils;

import android.app.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class PermissionManager {

    public void requestExternalStoragePermission(Activity activity) {
        if (checkSelfPermission(activity, STORAGE) != PERMISSION_GRANTED) {
            String[] list = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
            requestPermissions(activity, list, 1);
        }
    }
}
