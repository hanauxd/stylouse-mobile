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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.ProductAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentHomeBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.ProductService;
import retrofit2.Response;

public class HomeFragment extends HomeBaseFragment {
    private FragmentHomeBinding binding;
    private List<ProductResponse> products;
    private RecyclerView productList;

    @Inject
    ProductService productService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
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
        this.productList = binding.productList;
        productService.getProducts(productsCallback);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (state == null) {
            activity.getMenuInflater().inflate(R.menu.profile_items_unauth, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_sign_in: {
                parentNavController.navigate(R.id.signInFragment);
                break;
            }
            default: {}
        }
        return super.onOptionsItemSelected(item);
    }

    private ApiResponseCallback productsCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            products = (List<ProductResponse>) response.body();
            initRecyclerView();
            binding.layoutSpinner.setVisibility(View.GONE);
            binding.layoutHome.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(String message) {
            binding.layoutSpinner.setVisibility(View.GONE);
            binding.layoutHome.setVisibility(View.VISIBLE);
        }
    };

    private AdapterItemClickListener productClickListener = productJSON -> {
        Bundle bundle = new Bundle();
        bundle.putString("product", productJSON);
        parentNavController.navigate(R.id.action_mainFragment_to_productFragment, bundle);
    };

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        ProductAdapter productAdapter = new ProductAdapter(products, productClickListener);
        productList.setLayoutManager(gridLayoutManager);
        productList.setAdapter(productAdapter);
        productList.setVisibility(View.VISIBLE);
    }
}
