package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.databinding.CartListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.responses.CartResponse;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartResponse> carts;
    private AdapterItemClickListener listener;

    public CartAdapter(List<CartResponse> carts, AdapterItemClickListener listener) {
        this.carts = carts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartListItemBinding binding = CartListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        if (listener == null) {
            binding.btnRemove.setVisibility(View.GONE);
        }
        return new ViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(carts.get(position));
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CartListItemBinding binding;
        private AdapterItemClickListener listener;

        ViewHolder(@NonNull CartListItemBinding binding, AdapterItemClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        void bind(CartResponse cart) {
            binding.setCart(cart);
            binding.setTotal(String.valueOf(cart.getTotalPrice()));

            if (listener != null) {
                binding.btnRemove.setOnClickListener(v -> {
                    listener.onItemClick(cart.getId());
                    carts.remove(cart);
                });
            }

            String url = binding.getRoot().getResources().getString(R.string.baseURL)
                    .concat("product/images/download/")
                    .concat(cart.getProduct().getProductImages().get(0).getFilename());
            Glide.with(binding.getRoot())
                    .load(url)
                    .into(binding.productImage);
        }
    }
}
