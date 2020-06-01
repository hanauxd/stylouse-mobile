package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.CartAdapter;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentOrderDetailBinding;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import lk.apiit.eea.stylouse.models.responses.OrderItemResponse;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;

import static lk.apiit.eea.stylouse.databinding.FragmentOrderDetailBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;
import static lk.apiit.eea.stylouse.utils.StringFormatter.formatCurrency;

public class OrderDetailFragment extends AuthFragment {
    private FragmentOrderDetailBinding binding;
    private List<CartResponse> carts = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Order Details");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindOrderToView();
        initRecyclerView();
    }

    private void bindOrderToView() {
        String orderJSON = getArguments() != null ? getArguments().getString("order") : null;
        OrdersResponse ordersResponse = new Gson().fromJson(orderJSON, OrdersResponse.class);
        binding.setOrder(ordersResponse);
        binding.setTotal(formatCurrency(orderTotal(ordersResponse.getOrderItems())));
        carts = cartResponses(ordersResponse.getOrderItems());
    }

    private double orderTotal(List<OrderItemResponse> orderItems) {
        double total = 0;
        for (OrderItemResponse orderItem : orderItems) {
            total += orderItem.getQuantity() * orderItem.getProduct().getPrice();
        }
        return total;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity,
                RecyclerView.VERTICAL,
                false);
        CartAdapter adapter = new CartAdapter(carts,
                null,
                this::onProductClick);
        binding.orderItemList.setAdapter(adapter);
        binding.orderItemList.setLayoutManager(layoutManager);
    }

    private void onProductClick(String productJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("product", productJSON);
        navigate(parentNavController, R.id.action_orderDetailFragment_to_productFragment, bundle);
    }

    private List<CartResponse> cartResponses(List<OrderItemResponse> orderItems) {
        List<CartResponse> carts = new ArrayList<>();
        for (OrderItemResponse item : orderItems) {
            double subTotal = item.getQuantity() * item.getProduct().getPrice();
            CartResponse cart = new CartResponse(item.getProduct(),
                    item.getQuantity(),
                    item.getSize(),
                    subTotal);
            carts.add(cart);
        }
        return carts;
    }
}
