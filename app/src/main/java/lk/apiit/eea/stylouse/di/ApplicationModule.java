package lk.apiit.eea.stylouse.di;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.models.responses.SignInResponse;
import lk.apiit.eea.stylouse.services.AuthService;
import lk.apiit.eea.stylouse.services.CartService;
import lk.apiit.eea.stylouse.services.OrderService;
import lk.apiit.eea.stylouse.services.ProductService;
import lk.apiit.eea.stylouse.services.ReviewService;
import lk.apiit.eea.stylouse.services.UserService;
import lk.apiit.eea.stylouse.services.WishlistService;
import lk.apiit.eea.stylouse.utils.UrlBuilder;
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
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        GsonConverterFactory factory = GsonConverterFactory.create(gson);
        return new Retrofit
                .Builder()
                .baseUrl(context.getResources().getString(R.string.baseURL))
                .addConverterFactory(factory)
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

    @Provides
    @Singleton
    public ProductService provideProductServiceInstance(Retrofit retrofit) {
        return new ProductService(retrofit);
    }

    @Provides
    @Singleton
    public CartService provideCartServiceInstance(Retrofit retrofit) {
        return new CartService(retrofit);
    }

    @Provides
    @Singleton
    public OrderService provideOrderServiceInstance(Retrofit retrofit) {
        return new OrderService(retrofit);
    }

    @Provides
    @Singleton
    public WishlistService provideWishlistServiceInstance(Retrofit retrofit) {
        return new WishlistService(retrofit);
    }

    public ReviewService provideReviewServiceInstance(Retrofit retrofit) {
        return new ReviewService(retrofit);
    }

    @Provides
    @Singleton
    public UserService provideUserServiceInstance(Retrofit retrofit) {
        return new UserService(retrofit);
    }

    @Provides
    @Singleton
    public UrlBuilder provideUrlBuilderInstance(Context context) {
        return new UrlBuilder(context);
    }
}
