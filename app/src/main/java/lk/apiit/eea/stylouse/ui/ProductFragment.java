package lk.apiit.eea.stylouse.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

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
import lk.apiit.eea.stylouse.utils.UrlBuilder;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.bumptech.glide.Glide.with;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeError;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeSuccess;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeWarning;
import static java.util.Arrays.asList;
import static lk.apiit.eea.stylouse.databinding.FragmentProductBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

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
    @Inject
    UrlBuilder urlBuilder;

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
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle(R.string.app_name);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnRetry.setOnClickListener(this::fetchWishlist);
        renderReviewFragment();
        fetchWishlist(view);
        bindButtonsToClickListener();
        initSizeAdapter();
    }

    private void renderReviewFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("product", new Gson().toJson(product()));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.review_fragment_layout, ReviewFragment.class, bundle);
        transaction.commit();
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
        navigate(parentNavController, R.id.action_productFragment_to_signInFragment, null);
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

        if (isEmpty(productId) || isEmpty(adapter.getSize()) || quantity == 0) {
            makeWarning(activity, "Select size and quantity").show();
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
        adapter = new SizeAdapter(asList("S", "M", "L", "XL"));
        binding.sizeList.setAdapter(adapter);
    }

    private void bindProductToView() {
        binding.setProduct(product());
        with(binding.getRoot())
                .load(urlBuilder.fileUrl(product().getProductImages().get(0).getFilename()))
                .placeholder(R.drawable.stylouse_placeholder)
                .into(binding.productImage);
    }

    private ProductResponse product() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        return new Gson().fromJson(productJSON, ProductResponse.class);
    }

    private ApiResponseCallback addWishlistCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            wishlists = (List<WishlistResponse>) response.body();
            binding.btnWishlist.setImageResource(R.drawable.icon_favorite);
            binding.btnWishlist.setColorFilter(Color.RED);
            binding.btnWishlist.setClickable(true);
            makeSuccess(activity, "Product added to wishlist.").show();
        }

        @Override
        public void onFailure(String message) {
            binding.btnWishlist.setClickable(true);
            makeError(activity, message).show();
        }
    };

    private ApiResponseCallback deleteWishlistCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            wishlists = (List<WishlistResponse>) response.body();
            binding.btnWishlist.setImageResource(R.drawable.icon_wishlist);
            binding.btnWishlist.setColorFilter(Color.RED);
            binding.btnWishlist.setClickable(true);
            makeSuccess(activity, "Product removed from wishlist.").show();
        }

        @Override
        public void onFailure(String message) {
            binding.btnWishlist.setClickable(true);
            makeError(activity, message).show();
        }
    };

    private ApiResponseCallback isWishlistCallback = new ApiResponseCallback() {
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
            makeSuccess(activity, "Product added to cart.").show();
        }

        @Override
        public void onFailure(String message) {
            binding.btnCart.revertAnimation();
            makeError(activity, message).show();
        }
    };
}
