package lk.apiit.eea.stylouse.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.models.responses.SignInResponse;

import static lk.apiit.eea.stylouse.utils.Constants.USER_FILE;
import static lk.apiit.eea.stylouse.utils.Constants.USER_INFO;

public class UserStore {
    private Context context;

    @Inject
    public UserStore(Context context) {
        this.context = context;
    }

    public SignInResponse getUserDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_FILE, Context.MODE_PRIVATE);
        String userInfoString = sharedPreferences.getString(USER_INFO, null);
        if (userInfoString == null) {
            return null;
        }
        return new Gson().fromJson(userInfoString, SignInResponse.class);
    }

    public void putUserDetails(SignInResponse signInResponse) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userInfoString = new Gson().toJson(signInResponse);
        editor.putString(USER_INFO, userInfoString).apply();
    }

    public void clearUserDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}
