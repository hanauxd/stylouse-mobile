package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @Inject
    AuthSession session;
    @Inject
    CartService cartService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Shipping");
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
        bindOrderDetailsToView();
        btnOrder.setOnClickListener(this::onOrderClick);
    }

    private void bindOrderDetailsToView() {
        if (getArguments() == null) {
            return;
        }
        String total = getArguments().getString("total", "LKR 0.00");
        String numberOfItems = getArguments().getString("numberOfItems", "0");
        binding.setTotal(total);
        binding.setNumberOfItems(numberOfItems);
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
            parentNavController.navigate(R.id.action_shippingFragment_to_mainFragment);
        }

        @Override
        public void onFailure(String message) {
            btnOrder.revertAnimation();
            binding.errorMessage.setText(message);
            binding.errorMessage.setVisibility(View.VISIBLE);
        }
    };
}
