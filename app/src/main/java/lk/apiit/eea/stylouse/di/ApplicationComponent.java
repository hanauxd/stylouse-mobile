package lk.apiit.eea.stylouse.di;

import javax.inject.Singleton;

import dagger.Component;
import lk.apiit.eea.stylouse.MainActivity;
import lk.apiit.eea.stylouse.ui.CartFragment;
import lk.apiit.eea.stylouse.ui.HomeFragment;
import lk.apiit.eea.stylouse.ui.OrdersFragment;
import lk.apiit.eea.stylouse.ui.ProductFragment;
import lk.apiit.eea.stylouse.ui.ProfileFragment;
import lk.apiit.eea.stylouse.ui.SignInFragment;
import lk.apiit.eea.stylouse.ui.SignUpFragment;
import lk.apiit.eea.stylouse.ui.SplashFragment;
import lk.apiit.eea.stylouse.ui.WishlistFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity activity);
    void inject(ProfileFragment fragment);
    void inject(SignInFragment fragment);
    void inject(SignUpFragment fragment);
    void inject(SplashFragment fragment);
    void inject(HomeFragment fragment);
    void inject(CartFragment fragment);
    void inject(WishlistFragment fragment);
    void inject(OrdersFragment fragment);
    void inject(ProductFragment fragment);
}
