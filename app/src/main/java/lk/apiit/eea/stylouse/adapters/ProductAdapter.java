package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.ProductListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.interfaces.WishlistClickListener;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.utils.UrlBuilder;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    @Inject
    UrlBuilder urlBuilder;
    private List<ProductResponse> products;
    private AdapterItemClickListener productListener;
    private WishlistClickListener wishlistListener;

    public ProductAdapter(List<ProductResponse> products,
                          AdapterItemClickListener productListener,
                          WishlistClickListener wishlistListener) {
        this.products = products;
        this.productListener = productListener;
        this.wishlistListener = wishlistListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductListItemBinding binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        if (wishlistListener == null) {
            binding.btnWishlist.setVisibility(View.GONE);
        }
        ((StylouseApp)binding.getRoot().getContext().getApplicationContext()).getAppComponent().inject(this);
        return new ViewHolder(binding, productListener, wishlistListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ProductListItemBinding binding;
        private AdapterItemClickListener productListener;
        private WishlistClickListener wishlistListener;

        ViewHolder(@NonNull ProductListItemBinding binding, AdapterItemClickListener productListener, WishlistClickListener wishlistListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.productListener = productListener;
            this.wishlistListener = wishlistListener;
        }

        void bind(ProductResponse product) {
            binding.setProduct(product);

            String productJSON = new Gson().toJson(product);
            binding.root.setOnClickListener(view -> productListener.onItemClick(productJSON));

            if (wishlistListener != null) {
                binding.btnWishlist.setOnClickListener(view -> {
                    wishlistListener.onWishlistClick(product.getId());
                });
            }

            Glide.with(binding.getRoot())
                    .load(urlBuilder.fileUrl(product.getProductImages().get(0).getFilename()))
                    .placeholder(R.drawable.stylouse_placeholder)
                    .into(binding.thumbnail);
        }
    }
}
