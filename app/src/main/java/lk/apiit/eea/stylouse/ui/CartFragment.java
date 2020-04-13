package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import lk.apiit.eea.stylouse.utils.StringFormatter;
import retrofit2.Response;

public class CartFragment extends AuthFragment {
    private FragmentCartBinding binding;
    private List<CartResponse> carts = new ArrayList<>();
    private CartAdapter adapter;
    private NavController navController;

    @Inject
    AuthSession session;
    @Inject
    CartService cartService;

    private ApiResponseCallback getCartsCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            carts = (List<CartResponse>)response.body();
            bindCartToView();
            initRecyclerView();
            displayLayout();
        }

        @Override
        public void onFailure(String message) {
            displayLayout();
            DynamicToast.makeError(activity, message).show();
        }
    };

    private ApiResponseCallback deleteCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            DynamicToast.makeSuccess(activity, "Product removed successfully.").show();
            binding.setCount(String.valueOf(carts.size()));
            binding.setTotal(StringFormatter.formatCurrency(cartTotal()));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(String message) {
            DynamicToast.makeError(activity, "Failed to remove product.").show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Cart");
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
        navController = Navigation.findNavController(view);

        if (session.getAuthState() != null) {
            String token = session.getAuthState().getJwt();
            cartService.getCarts(getCartsCallback, token);
        }
    }

    private void bindCartToView() {
        binding.setCount(String.valueOf(carts.size()));
        binding.setTotal(StringFormatter.formatCurrency(cartTotal()));
        binding.btnCheckout.setOnClickListener(this::onCheckoutClick);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        adapter = new CartAdapter(carts, this::onDeleteCartClick, this::onProductClick);
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.setAdapter(adapter);
    }

    private void onDeleteCartClick(String cartId) {
        cartService.deleteCart(
                deleteCallback,
                session.getAuthState().getJwt(),
                cartId);
    }

    private void onProductClick(String productJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("product", productJSON);
        navController.navigate(R.id.action_navigation_cart_to_productFragment, bundle);
    }

    private void onCheckoutClick(View view) {
        if (carts.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("total", "LKR ".concat(StringFormatter.formatCurrency(cartTotal())));
            bundle.putString("numberOfItems", String.valueOf(carts.size()));
            navController.navigate(R.id.action_navigation_cart_to_shippingFragment, bundle);
        } else {
            DynamicToast.make(activity, "Add items to cart.").show();
        }
    }

    private double cartTotal() {
        double total = 0;
        for (CartResponse cart : carts) total += cart.getTotalPrice();
        return total;
    }

    private void displayLayout() {
        binding.layoutSpinner.setVisibility(View.GONE);
        binding.layoutCart.setVisibility(View.VISIBLE);
    }
}
