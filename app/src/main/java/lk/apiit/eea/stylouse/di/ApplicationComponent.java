package lk.apiit.eea.stylouse.di;

import javax.inject.Singleton;

import dagger.Component;
import lk.apiit.eea.stylouse.MainActivity;
import lk.apiit.eea.stylouse.ui.HomeBaseFragment;
import lk.apiit.eea.stylouse.ui.ProfileFragment;
import lk.apiit.eea.stylouse.ui.SignInFragment;
import lk.apiit.eea.stylouse.ui.SignUpFragment;
import lk.apiit.eea.stylouse.ui.SplashFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity activity);
    void inject(HomeBaseFragment fragment);
    void inject(ProfileFragment fragment);
    void inject(SignInFragment fragment);
    void inject(SignUpFragment fragment);
    void inject(SplashFragment fragment);
}
