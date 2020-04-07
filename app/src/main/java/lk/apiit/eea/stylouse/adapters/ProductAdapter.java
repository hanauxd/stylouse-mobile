package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.databinding.ProductListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProductResponse> products;
    private AdapterItemClickListener listener;

    public ProductAdapter(List<ProductResponse> products, AdapterItemClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductListItemBinding binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ProductListItemBinding binding;
        private AdapterItemClickListener listener;

        ViewHolder(@NonNull ProductListItemBinding binding, AdapterItemClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        void bind(ProductResponse product) {
            binding.setProduct(product);

            String productJSON = new Gson().toJson(product);
            binding.root.setOnClickListener(view -> listener.onItemClick(productJSON));

            String url = binding.getRoot().getResources().getString(R.string.baseURL) +
                    "product/images/download/" +
                    product.getProductImages().get(0).getFilename();
            Glide.with(binding.getRoot())
                    .load(url)
                    .into(binding.thumbnail);
        }
    }
}
