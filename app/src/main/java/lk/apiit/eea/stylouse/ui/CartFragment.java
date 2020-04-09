package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import lk.apiit.eea.stylouse.services.CartService;
import retrofit2.Response;

public class CartFragment extends AuthFragment implements ApiResponseCallback, AdapterItemClickListener {
    private FragmentCartBinding binding;
    private List<CartResponse> carts = new ArrayList<>();
    private CartAdapter adapter;
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
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        String token = session.getAuthState().getJwt();
        cartService.getCarts(this, token);
    }

    @Override
    public void onSuccess(Response<?> response) {
        this.carts = (List<CartResponse>)response.body();
        bindCartDetails();
        initRecyclerView();
        displayLayout();
    }

    @Override
    public void onFailure(String message) {
        displayLayout();
        DynamicToast.makeError(activity, message).show();
    }

    @Override
    public void onItemClick(String cartId) {
        cartService.deleteCart(new CartHandler(), session.getAuthState().getJwt(), cartId);
    }

    private void displayLayout() {
        binding.layoutSpinner.setVisibility(View.GONE);
        binding.layoutCart.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        adapter = new CartAdapter(carts, this);
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.setAdapter(adapter);
    }

    private void bindCartDetails() {
        binding.setCount(String.valueOf(carts.size()));
        binding.setTotal(String.valueOf(cartTotal()));
        binding.btnCheckout.setOnClickListener(v -> navController.navigate(R.id.action_navigation_cart_to_shippingFragment));
    }

    private double cartTotal() {
        double total = 0;
        for (CartResponse cart : carts) total += cart.getTotalPrice();
        return total;
    }

    class CartHandler implements ApiResponseCallback {

        @Override
        public void onSuccess(Response<?> response) {
            DynamicToast.makeSuccess(activity, "Product removed successfully.").show();
            binding.setCount(String.valueOf(carts.size()));
            binding.setTotal(String.valueOf(cartTotal()));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(String message) {
            DynamicToast.makeError(activity, "Failed to remove product.").show();
        }
    }
}
