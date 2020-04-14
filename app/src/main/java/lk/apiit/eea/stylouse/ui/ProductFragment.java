package lk.apiit.eea.stylouse.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.SizeAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentProductBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.requests.CartRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.models.responses.WishlistResponse;
import lk.apiit.eea.stylouse.services.CartService;
import lk.apiit.eea.stylouse.services.WishlistService;
import retrofit2.Response;

public class ProductFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentProductBinding binding;
    private SizeAdapter adapter;
    private List<WishlistResponse> wishlists;

    @Inject
    AuthSession session;
    @Inject
    CartService cartService;
    @Inject
    WishlistService wishlistService;

    private ApiResponseCallback addWishlistCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            wishlists = (List<WishlistResponse>) response.body();
            binding.btnWishlist.setImageResource(R.drawable.icon_favorite);
            binding.btnWishlist.setColorFilter(Color.RED);
            binding.btnWishlist.setClickable(true);
            DynamicToast.makeSuccess(activity, "Product added to wishlist.").show();
        }

        @Override
        public void onFailure(String message) {
            binding.btnWishlist.setClickable(true);
            DynamicToast.makeError(activity, message).show();
        }
    };

    private ApiResponseCallback deleteWishlistCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            wishlists = (List<WishlistResponse>)response.body();
            binding.btnWishlist.setImageResource(R.drawable.icon_wishlist);
            binding.btnWishlist.setColorFilter(Color.RED);
            binding.btnWishlist.setClickable(true);
            DynamicToast.makeSuccess(activity, "Product removed from wishlist.").show();
        }

        @Override
        public void onFailure(String message) {
            binding.btnWishlist.setClickable(true);
            DynamicToast.makeError(activity, message).show();
        }
    };

    private  ApiResponseCallback isWishlistCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            bindProductToView();
            wishlists = (List<WishlistResponse>) response.body();
            for (WishlistResponse wishlist : wishlists) {
                if (wishlist.getProduct().getId().equals(binding.getProduct().getId())) {
                    binding.btnWishlist.setImageResource(R.drawable.icon_favorite);
                    binding.btnWishlist.setColorFilter(Color.RED);
                }
            }
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };

    private ApiResponseCallback addCartCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            binding.btnCart.revertAnimation();
            DynamicToast.makeSuccess(activity, "Product added to cart.").show();
        }

        @Override
        public void onFailure(String message) {
            binding.btnCart.revertAnimation();
            DynamicToast.makeError(activity, message).show();
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
        binding = FragmentProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle(R.string.app_name);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnRetry.setOnClickListener(this::fetchWishlist);
        fetchWishlist(view);
        bindButtonsToClickListener();
        initSizeAdapter();
    }

    private void fetchWishlist(View view) {
        if (session.getAuthState() != null) {
            loading.setValue(true);
            wishlistService.getWishlist(isWishlistCallback, session.getAuthState().getJwt());
        } else {
            bindProductToView();
        }
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean aBoolean) {
        binding.setLoading(aBoolean);
    }

    private void bindButtonsToClickListener() {
        if (session.getAuthState() == null) {
            binding.btnWishlist.setVisibility(View.GONE);
            binding.btnCart.setVisibility(View.GONE);
            binding.btnSignIn.setOnClickListener(this::onSignInClick);
        } else {
            binding.btnSignIn.setVisibility(View.GONE);
            binding.btnWishlist.setOnClickListener(this::onAddToWishlistClick);
            binding.btnCart.setOnClickListener(this::onAddToCartClick);
            binding.btnWishlist.setColorFilter(Color.RED);
        }
    }

    private void onSignInClick(View view) {
        parentNavController.navigate(R.id.action_productFragment_to_signInFragment);
    }

    private void onAddToWishlistClick(View view) {
        binding.btnWishlist.setClickable(false);
        binding.btnWishlist.setColorFilter(Color.LTGRAY);
        for (WishlistResponse wishlist : wishlists) {
            if (wishlist.getProduct().getId().equals(binding.getProduct().getId())) {
                wishlistService.deleteWishlist(wishlist.getId(), deleteWishlistCallback, session.getAuthState().getJwt());
                return;
            }
        }

        HashMap<String, String> wishlistRequest = new HashMap<>();
        wishlistRequest.put("productId", binding.getProduct().getId());
        wishlistService.addWishlist(
                wishlistRequest,
                addWishlistCallback,
                session.getAuthState().getJwt()
        );
    }

    private void onAddToCartClick(View view) {
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
    }

    private void initSizeAdapter() {
        adapter = new SizeAdapter(Arrays.asList("S", "M", "L", "XL"));
        binding.sizeList.setAdapter(adapter);
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
                .placeholder(R.drawable.stylouse_placeholder)
                .into(binding.productImage);
    }
}
