package lk.apiit.eea.stylouse.di;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.models.responses.SignInResponse;

public class AuthSession {
    private MutableLiveData<SignInResponse> state = new MutableLiveData<>();
    private UserStore store;

    @Inject
    public AuthSession(UserStore store) {
        this.store = store;
        state.setValue(store.getUserDetails());
    }

    public void setAuthState(SignInResponse response) {
        state.setValue(response);
        if (response == null) {
            store.clearUserDetails();
        } else {
            store.putUserDetails(response);
        }
    }

    public SignInResponse getAuthState() {
        return state.getValue();
    }

    public void setObserver(LifecycleOwner lc, Observer<SignInResponse> o) {
        state.observe(lc, o);
    }

    public void removeObserver(LifecycleOwner lc) {
        state.removeObservers(lc);
    }
}
