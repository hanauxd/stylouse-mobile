package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.CartAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentCartBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import lk.apiit.eea.stylouse.services.CartService;
import lk.apiit.eea.stylouse.utils.Navigator;
import lk.apiit.eea.stylouse.utils.StringFormatter;
import retrofit2.Response;

public class CartFragment extends AuthFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private MutableLiveData<Integer> count = new MutableLiveData<>(0);
    private List<CartResponse> carts = new ArrayList<>();
    private FragmentCartBinding binding;

    @Inject
    AuthSession session;
    @Inject
    CartService cartService;

    private ApiResponseCallback cartCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            carts = (List<CartResponse>)response.body();
            bindCartToView();
            initRecyclerView();
            loading.setValue(false);
            count.setValue(carts.size());
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
            count.setValue(0);
        }
    };

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
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Cart");
        binding.btnRetry.setOnClickListener(this::fetchCartItems);

        if (session.getAuthState() != null) {
            loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
            error.observe(getViewLifecycleOwner(), this::onErrorChange);
            count.observe(getViewLifecycleOwner(), this::onCountChange);
            fetchCartItems(view);
        }
    }

    private void onCountChange(Integer count) {
        binding.setCount(count);
    }

    private void fetchCartItems(View view) {
        loading.setValue(true);
        if (error.getValue() != null) {
            error.setValue(null);
        }
        cartService.getCarts(cartCallback, session.getAuthState().getJwt());
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void bindCartToView() {
        binding.setCount(carts.size());
        binding.setTotal(StringFormatter.formatCurrency(cartTotal()));
        binding.btnCheckout.setOnClickListener(this::onCheckoutClick);
    }

    private void initRecyclerView() {
        CartAdapter adapter = new CartAdapter(carts, this::onDeleteCartClick, this::onProductClick);
        binding.cartList.setAdapter(adapter);
    }

    private void onDeleteCartClick(String cartId) {
        loading.setValue(true);
        error.setValue(null);
        cartService.deleteCart(
                cartCallback,
                session.getAuthState().getJwt(),
                cartId);
    }

    private void onProductClick(String productJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("product", productJSON);
        Navigator.navigate(parentNavController, R.id.action_mainFragment_to_productFragment, bundle);
    }

    private void onCheckoutClick(View view) {
        if (carts.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("total", "LKR ".concat(StringFormatter.formatCurrency(cartTotal())));
            bundle.putString("numberOfItems", String.valueOf(carts.size()));
            Navigator.navigate(parentNavController, R.id.shippingFragment, bundle);
        } else {
            DynamicToast.make(activity, "Add items to cart.").show();
        }
    }

    private double cartTotal() {
        double total = 0;
        for (CartResponse cart : carts) total += cart.getTotalPrice();
        return total;
    }
}
