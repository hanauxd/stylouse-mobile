package lk.apiit.eea.stylouse.application;

import android.app.Application;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.di.ApplicationComponent;
import lk.apiit.eea.stylouse.di.ApplicationModule;
import lk.apiit.eea.stylouse.di.DaggerApplicationComponent;

public class StylouseApp extends Application{
    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule((this))).build();
        DynamicToast.Config.getInstance().setWarningBackgroundColor(getResources().getColor(R.color.colorWarning, null)).apply();
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}
