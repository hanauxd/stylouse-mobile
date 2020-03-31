package lk.apiit.eea.stylouse.di;

import android.content.Context;
import android.content.SharedPreferences;

public class UserStore {

    private String USER_FILE = "USER_FILE";
    private String USER_INFO = "USER_INFO";

    public boolean getUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(USER_INFO, false);
    }
}
