package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.ProductAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentHomeBinding;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.ProductService;
import lk.apiit.eea.stylouse.utils.Navigator;
import retrofit2.Response;

public class HomeFragment extends HomeBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentHomeBinding binding;
    private List<ProductResponse> products;

    @Inject
    ProductService productService;

    private ApiResponseCallback productsCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            products = (List<ProductResponse>) response.body();
            initRecyclerView();
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
        ActionBar appBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (appBar != null && !appBar.isShowing()) {
            appBar.show();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle(R.string.app_name);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnRetry.setOnClickListener(this::fetchProducts);
        fetchProducts(view);
    }

    private void fetchProducts(View view) {
        if (error.getValue() != null) error.setValue(null);
        loading.setValue(true);
        productService.getProducts(productsCallback);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean aBoolean) {
        binding.setLoading(aBoolean);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (state == null) {
            activity.getMenuInflater().inflate(R.menu.profile_items_unauth, menu);
        } else if (state.getUserRole().equals("ROLE_ADMIN")) {
            activity.getMenuInflater().inflate(R.menu.admin_items, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sign_in) {
            Navigator.navigate(parentNavController, R.id.signInFragment, null);
        } else if (item.getItemId() == R.id.action_add_product) {
            Navigator.navigate(parentNavController, R.id.action_adminFragment_to_addProductFragment, null);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(products, this::onProductClick, null);
        binding.productList.setAdapter(productAdapter);
    }

    private void onProductClick(String productJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("product", productJSON);
        if (state == null || state.getUserRole().equals("ROLE_USER")) {
            Navigator.navigate(parentNavController, R.id.action_mainFragment_to_productFragment, bundle);
        } else {
            Navigator.navigate(parentNavController, R.id.action_adminFragment_to_editProductFragment, bundle);
        }
    }
}
