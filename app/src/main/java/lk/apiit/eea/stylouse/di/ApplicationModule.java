package lk.apiit.eea.stylouse.di;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContextInstance() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofitInstance(Context context) {
        return new Retrofit
                .Builder()
                .baseUrl(context.getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public UserStore provideUserStoreInstance(Context context) {
        return new UserStore(context);
    }

    @Provides
    @Singleton
    public AuthSession provideAuthSession(UserStore store) {
        return new AuthSession(store);
    }

    @Nullable
    @Provides
    public SignInResponse provideSignInResponse(AuthSession session) {
        return session.getAuthState();
    }

    @Provides
    @Singleton
    public AuthService provideAuthServiceInstance(Retrofit retrofit) {
        return new AuthService(retrofit);
    }
}
