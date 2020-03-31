package lk.apiit.eea.stylouse.application;

import android.app.Application;

import lk.apiit.eea.stylouse.di.ApplicationComponent;
import lk.apiit.eea.stylouse.di.DaggerApplicationComponent;

public class StylouseApp extends Application{
    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.create();
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}
