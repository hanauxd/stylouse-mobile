package lk.apiit.eea.stylouse.di;

import javax.inject.Singleton;

import dagger.Component;
import lk.apiit.eea.stylouse.MainActivity;
import lk.apiit.eea.stylouse.adapters.CartAdapter;
import lk.apiit.eea.stylouse.adapters.ProductAdapter;
import lk.apiit.eea.stylouse.ui.CartFragment;
import lk.apiit.eea.stylouse.ui.ForgotPasswordFragment;
import lk.apiit.eea.stylouse.ui.HomeFragment;
import lk.apiit.eea.stylouse.ui.OrderDetailFragment;
import lk.apiit.eea.stylouse.ui.OrdersFragment;
import lk.apiit.eea.stylouse.ui.OtpFragment;
import lk.apiit.eea.stylouse.ui.ProductFragment;
import lk.apiit.eea.stylouse.ui.ProfileFragment;
import lk.apiit.eea.stylouse.ui.RateFragment;
import lk.apiit.eea.stylouse.ui.ResetPasswordFragment;
import lk.apiit.eea.stylouse.ui.ReviewFragment;
import lk.apiit.eea.stylouse.ui.ShippingFragment;
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
    void inject(ShippingFragment fragment);
    void inject(OrderDetailFragment fragment);
    void inject(ForgotPasswordFragment fragment);
    void inject(OtpFragment fragment);
    void inject(ResetPasswordFragment fragment);
    void inject(ReviewFragment fragment);
    void inject(RateFragment fragment);

    void inject(ProductAdapter adapter);
    void inject(CartAdapter adapter);
}
