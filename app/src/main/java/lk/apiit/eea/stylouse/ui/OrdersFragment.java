package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.OrderAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentOrdersBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;
import lk.apiit.eea.stylouse.services.OrderService;
import retrofit2.Response;

import static lk.apiit.eea.stylouse.databinding.FragmentOrdersBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class OrdersFragment extends AuthFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private MutableLiveData<Integer> count = new MutableLiveData<>(0);
    private FragmentOrdersBinding binding;
    private List<OrdersResponse> orders = new ArrayList<>();

    @Inject
    OrderService orderService;
    @Inject
    AuthSession session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
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
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Orders");
        if (session.getAuthState() != null) {
            loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
            error.observe(getViewLifecycleOwner(), this::onErrorChange);
            count.observe(getViewLifecycleOwner(), this::onCountChange);
            binding.btnRetry.setOnClickListener(this::fetchOrderItems);
            fetchOrderItems(view);
        }
    }

    private void onCountChange(Integer count) {
        binding.setCount(count);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void fetchOrderItems(View view) {
        if (error.getValue() != null) error.setValue(null);
        loading.setValue(true);
        orderService.getOrder(orderListCallback, session.getAuthState().getJwt());
    }

    private ApiResponseCallback orderListCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            orders = (List<OrdersResponse>) response.body();
            loading.setValue(false);
            count.setValue(orders.size());
            initRecyclerView();
        }

        @Override
        public void onFailure(String message) {
            error.setValue(message);
            loading.setValue(false);
            count.setValue(0);
        }
    };

    private void initRecyclerView() {
        OrderAdapter adapter = new OrderAdapter(orders, this::onOrderClick);
        binding.ordersList.setAdapter(adapter);
    }

    private void onOrderClick(String orderJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("order", orderJSON);
        navigate(parentNavController, R.id.action_mainFragment_to_orderDetailFragment, bundle);
    }
}
