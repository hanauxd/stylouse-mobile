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
import lk.apiit.eea.stylouse.adapters.ProductAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentWishlistBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.models.responses.WishlistResponse;
import lk.apiit.eea.stylouse.services.WishlistService;
import retrofit2.Response;

import static lk.apiit.eea.stylouse.databinding.FragmentWishlistBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class WishlistFragment extends AuthFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private MutableLiveData<Integer> count = new MutableLiveData<>(0);
    private FragmentWishlistBinding binding;
    private List<WishlistResponse> wishlists;
    private List<ProductResponse> products = new ArrayList<>();;

    @Inject
    WishlistService wishlistService;
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
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Wishlist");
        if (session.getAuthState() != null) {
            binding.btnRetry.setOnClickListener(this::fetchWishlistItems);
            loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
            error.observe(getViewLifecycleOwner(), this::onErrorChange);
            count.observe(getViewLifecycleOwner(), this::onCountChange);
            fetchWishlistItems(view);
        }
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onCountChange(Integer count) {
        binding.setCount(count);
    }

    private void fetchWishlistItems(View view) {
        if (error.getValue() != null) error.setValue(null);
        loading.setValue(true);
        wishlistService.getWishlist(wishlistCallback, session.getAuthState().getJwt());
    }

    private void initProductAdapter() {
        if (products != null) {
            ProductAdapter adapter = new ProductAdapter(products, this::onProductClick, this::onWishlistClick);
            binding.wishlistList.setAdapter(adapter);
        }
    }

    private void onProductClick(String productJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("product", productJSON);
        navigate(parentNavController, R.id.action_mainFragment_to_productFragment, bundle);
    }

    private void onWishlistClick(String productId) {
        loading.setValue(true);
        for (WishlistResponse item : wishlists) {
            if (productId.equals(item.getProduct().getId())) {
                wishlistService.deleteWishlist(
                        item.getId(),
                        wishlistCallback,
                        session.getAuthState().getJwt()
                );
            }
        }
    }

    private ApiResponseCallback wishlistCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            wishlists = (List<WishlistResponse>) response.body();
            products.clear();
            for (WishlistResponse wishlist : wishlists) {
                products.add(wishlist.getProduct());
            }
            initProductAdapter();
            count.setValue(wishlists.size());
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
            count.setValue(0);
        }
    };
}
