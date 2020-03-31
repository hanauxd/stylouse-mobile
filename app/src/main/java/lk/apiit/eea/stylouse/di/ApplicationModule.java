package lk.apiit.eea.stylouse.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lk.apiit.eea.stylouse.services.AuthService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static lk.apiit.eea.stylouse.utils.Constants.BASE_URL;

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    public Retrofit provideRetrofitInstance() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public UserStore provideUserStoreInstance() {
        return new UserStore();
    }

    @Provides
    @Singleton
    public AuthService provideAuthServiceInstance(Retrofit retrofit) {
        return new AuthService(retrofit);
    }
}
