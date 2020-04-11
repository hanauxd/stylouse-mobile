package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Arrays;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.SizeAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentProductBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.requests.CartRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.CartService;
import retrofit2.Response;

public class ProductFragment extends RootBaseFragment {
    private FragmentProductBinding binding;
    private RecyclerView productSizeList;
    private SizeAdapter adapter;

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
        binding = FragmentProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.productSizeList = binding.sizeList;
        binding.btnWishlist.setOnClickListener(this::onAddToWishlistClick);
        binding.btnCart.setOnClickListener(this::onAddToCartClick);
        initRecyclerView();
        bindProductToView();
    }

    private ApiResponseCallback addCartCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            binding.btnCart.revertAnimation();
            DynamicToast.makeSuccess(activity, "Product added to cart.").show();
            parentNavController.navigate(R.id.action_productFragment_to_mainFragment);
        }

        @Override
        public void onFailure(String message) {
            binding.btnCart.revertAnimation();
            DynamicToast.makeError(activity, message).show();
        }
    };

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 4);
        adapter = new SizeAdapter(Arrays.asList("S", "M", "L", "XL"));
        productSizeList.setLayoutManager(layoutManager);
        productSizeList.setAdapter(adapter);
    }

    private void bindProductToView() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        ProductResponse product = new Gson().fromJson(productJSON, ProductResponse.class);
        binding.setProduct(product);

        String url = binding.getRoot().getResources().getString(R.string.baseURL)
                + "product/images/download/"
                + product.getProductImages().get(0).getFilename();
        Glide.with(binding.getRoot())
                .load(url)
                .into(binding.productImage);
    }

    private void onAddToCartClick(View view) {
        if (session.getAuthState() != null) {
            int quantity = binding.numberPicker.getProgress();
            String productId = binding.getProduct().getId();

            if (TextUtils.isEmpty(productId) || TextUtils.isEmpty(adapter.getSize()) || quantity == 0) {
                DynamicToast.makeWarning(activity, "Select size and quantity").show();
            } else {
                binding.btnCart.startAnimation();
                cartService.addCart(
                        new CartRequest(productId, adapter.getSize(), quantity),
                        addCartCallback,
                        session.getAuthState().getJwt()
                );
            }
        } else {
            DynamicToast.makeError(activity, "Sign in to continue.").show();
        }
    }
}
