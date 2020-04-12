package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

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

public class WishlistFragment extends AuthFragment {
    private FragmentWishlistBinding binding;
    private NavController navController;
    private ProductAdapter adapter;
    private List<WishlistResponse> wishlists;
    private List<ProductResponse> products = new ArrayList<>();

    @Inject
    WishlistService wishlistService;
    @Inject
    AuthSession session;

    private ApiResponseCallback wishlistCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            wishlists = (List<WishlistResponse>) response.body();
             for (WishlistResponse wishlist : wishlists) {
                 products.add(wishlist.getProduct());
             }
             initRecyclerView();
        }

        @Override
        public void onFailure(String message) {
            DynamicToast.makeError(activity, message).show();
        }
    };

    private ApiResponseCallback deleteCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            adapter.notifyDataSetChanged();
            DynamicToast.makeSuccess(activity, "Product removed from wishlist.").show();
        }

        @Override
        public void onFailure(String message) {
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
        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        wishlistService.getWishlist(wishlistCallback, session.getAuthState().getJwt());
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        adapter = new ProductAdapter(products, this::onProductClick, this::onWishlistClick);
        binding.wishlistList.setLayoutManager(layoutManager);
        binding.wishlistList.setAdapter(adapter);
    }

    private void onProductClick(String productJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("product", productJSON);
        navController.navigate(R.id.action_navigation_wishlist_to_productFragment, bundle);
    }

    private void onWishlistClick(String productId) {
        for (WishlistResponse item : wishlists) {
            if (productId.equals(item.getProduct().getId())) {
                wishlistService.deleteWishlist(item.getId(), deleteCallback, session.getAuthState().getJwt());
            }
        }
    }
}
