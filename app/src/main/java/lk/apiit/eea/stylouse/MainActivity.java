package lk.apiit.eea.stylouse;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import lk.apiit.eea.stylouse.dto.requests.AuthRequest;
import lk.apiit.eea.stylouse.dto.responses.AuthResponse;
import lk.apiit.eea.stylouse.interfaces.AuthInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @BindString(R.string.baseURL) String BASE_URL;
    @BindView(R.id.action_text) TextView displayMessage;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_password) EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        handleButtonClick();
    }

    private void handleButtonClick() {
        Retrofit retrofit = getClient();
        final AuthInterface authInterface = retrofit.create(AuthInterface.class);
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            AuthRequest request = new AuthRequest(username, password);
            Call<AuthResponse> call = authInterface.login(request);
            call.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    Log.d("[STATUS_CODE]", String.valueOf(response.code()));
                    AuthResponse authResponse = response.body();
                    Log.e("[RESPONSE]", authResponse.toString());
                    displayMessage.setText(authResponse.getJwt());
                    //TODO - handle different response types
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Log.e("[LOGIN_FAILED]", t.getMessage());
                    call.cancel();
                    displayMessage.setText(t.getMessage());
                }
            });
        });
    }

    private Retrofit getClient() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
