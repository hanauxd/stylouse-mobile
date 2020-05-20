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
import lk.apiit.eea.stylouse.databinding.CartListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.responses.CartResponse;
import lk.apiit.eea.stylouse.utils.StringFormatter;
import lk.apiit.eea.stylouse.utils.UrlBuilder;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    @Inject
    UrlBuilder urlBuilder;
    private List<CartResponse> carts;
    private AdapterItemClickListener deleteClickListener;
    private AdapterItemClickListener productClickListener;

    public CartAdapter(
            List<CartResponse> carts,
            AdapterItemClickListener deleteClickListener,
            AdapterItemClickListener productClickListener) {
        this.carts = carts;
        this.deleteClickListener = deleteClickListener;
        this.productClickListener = productClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartListItemBinding binding = CartListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        if (deleteClickListener == null) {
            binding.btnRemove.setVisibility(View.GONE);
        }
        ((StylouseApp)binding.getRoot().getContext().getApplicationContext()).getAppComponent().inject(this);
        return new ViewHolder(binding, deleteClickListener, productClickListener);
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
        private AdapterItemClickListener deleteClickListener;
        private AdapterItemClickListener productClickListener;
        private CartResponse cart;

        ViewHolder(
                @NonNull CartListItemBinding binding,
                AdapterItemClickListener deleteClickListener,
                AdapterItemClickListener productClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.deleteClickListener = deleteClickListener;
            this.productClickListener = productClickListener;
        }

        void bind(CartResponse cart) {
            this.cart = cart;
            binding.setCart(cart);
            binding.setTotal(StringFormatter.formatCurrency(cart.getTotalPrice()));
            binding.product.setOnClickListener(this::onProductClick);
            if (deleteClickListener != null) {
                binding.btnRemove.setOnClickListener(this::onDeleteClick);
            }
            bindImageToView();
        }

        private void onDeleteClick(View view) {
            deleteClickListener.onItemClick(cart.getId());
        }

        private void onProductClick(View view) {
            productClickListener.onItemClick(
                    new Gson()
                    .toJson(cart.getProduct())
            );
        }

        private void bindImageToView() {
            Glide.with(binding.getRoot())
                    .load(urlBuilder.fileUrl(cart.getProduct().getProductImages().get(0).getFilename()))
                    .placeholder(R.drawable.stylouse_placeholder)
                    .into(binding.productImage);
        }
    }
}
