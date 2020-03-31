package lk.apiit.eea.stylouse.di;

import javax.inject.Singleton;

import dagger.Component;
import lk.apiit.eea.stylouse.MainActivity;
import lk.apiit.eea.stylouse.ui.HomeBaseFragment;
import lk.apiit.eea.stylouse.ui.ProfileFragment;
import lk.apiit.eea.stylouse.ui.SignInFragment;
import lk.apiit.eea.stylouse.ui.SignUpFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(HomeBaseFragment homeBaseFragment);
    void inject(ProfileFragment profileFragment);
    void inject(SignInFragment signInFragment);
    void inject(SignUpFragment signUpFragment);
}
