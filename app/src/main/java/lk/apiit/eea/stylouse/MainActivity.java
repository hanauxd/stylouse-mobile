package lk.apiit.eea.stylouse;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.ActivityMainBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.ActivityHandler;

public class MainActivity extends AppCompatActivity implements ActivityHandler, Runnable {
    @Inject
    AuthSession session;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp)getApplication()).getAppComponent().inject(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    public void run() {
        session.setAuthState(null);
    }

    @Override
    public void create(long duration) {
        handler = new Handler();
        handler.postDelayed(this, duration);
    }

    @Override
    public void destroy() {
        if (handler != null && handler.hasCallbacks(this)) {
            handler.removeCallbacks(this);
        }
    }
}
