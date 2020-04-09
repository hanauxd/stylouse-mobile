package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.adapters.OrderAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentOrdersBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;
import lk.apiit.eea.stylouse.services.OrderService;
import retrofit2.Response;

public class OrdersFragment extends AuthFragment {
    private FragmentOrdersBinding binding;
    private List<OrdersResponse> orders = new ArrayList<>();

    private MutableLiveData<List<OrdersResponse>> ordersData = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private MutableLiveData<String> error = new MutableLiveData<>(null);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        ordersData.observe(getViewLifecycleOwner(), this::onOrdersChange);

        String jwt = session.getAuthState().getJwt();
        orderService.getOrder(orderListCallback, jwt);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onOrdersChange(List<OrdersResponse> ordersResponses) {
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private ApiResponseCallback orderListCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            orders = (List<OrdersResponse>) response.body();
            loading.setValue(false);
            initRecyclerView();
        }

        @Override
        public void onFailure(String message) {
            DynamicToast.makeError(activity, message).show();
            error.setValue(message);
            loading.setValue(false);
        }
    };

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        OrderAdapter adapter = new OrderAdapter(orders);
        binding.ordersList.setLayoutManager(layoutManager);
        binding.ordersList.setAdapter(adapter);
    }
}
