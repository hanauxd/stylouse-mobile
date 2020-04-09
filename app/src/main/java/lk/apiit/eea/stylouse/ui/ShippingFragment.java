package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import javax.inject.Inject;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentShippingBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.requests.ShippingRequest;
import lk.apiit.eea.stylouse.services.CartService;
import retrofit2.Response;

public class ShippingFragment extends AuthFragment {
    private FragmentShippingBinding binding;
    private CircularProgressButton btnOrder;
    private NavController navController;

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
        binding = FragmentShippingBinding.inflate(inflater, container, false);
        btnOrder = binding.btnOrder;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnOrder.setOnClickListener(this::onOrderClick);
    }

    private void onOrderClick(View view) {
        String address = binding.address.getText().toString();
        String city = binding.city.getText().toString();
        String postalCode = binding.postalCode.getText().toString();

        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(city) || TextUtils.isEmpty(postalCode)) {
            binding.errorMessage.setText(R.string.error_field);
            binding.errorMessage.setVisibility(View.VISIBLE);
        } else {
            btnOrder.startAnimation();
            ShippingRequest shipping = new ShippingRequest(address, city, postalCode, "Cash On Delivery");
            cartService.checkout(checkoutCallback, shipping, session.getAuthState().getJwt());
        }
    }

    private ApiResponseCallback checkoutCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            btnOrder.revertAnimation();
            DynamicToast.makeSuccess(activity, "Order placed successfully.").show();
            navController.navigate(R.id.navigation_home);
        }

        @Override
        public void onFailure(String message) {
            btnOrder.revertAnimation();
            binding.errorMessage.setText(message);
            binding.errorMessage.setVisibility(View.VISIBLE);
        }
    };
}
