package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentCartBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import lk.apiit.eea.stylouse.services.CartService;
import retrofit2.Response;

public class CartFragment extends AuthFragment implements ApiResponseCallback {
    private FragmentCartBinding binding;
    private List<CartResponse> carts = new ArrayList<>();

    @Inject
    AuthSession session;
    @Inject
    CartService cartService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String token = session.getAuthState().getJwt();
        cartService.getCarts(this, token);
    }

    @Override
    public void onSuccess(Response<?> response) {
        this.carts = (List<CartResponse>)response.body();
        displayLayout();
    }

    @Override
    public void onFailure(String message) {
        displayLayout();
        DynamicToast.makeError(activity, message).show();
    }

    private void displayLayout() {
        binding.layoutSpinner.setVisibility(View.GONE);
        binding.layoutCart.setVisibility(View.VISIBLE);
    }
}
