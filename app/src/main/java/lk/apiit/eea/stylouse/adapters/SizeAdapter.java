package lk.apiit.eea.stylouse.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.databinding.ProductSizeBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;

import static android.view.LayoutInflater.from;
import static lk.apiit.eea.stylouse.databinding.ProductSizeBinding.inflate;

public class SizeAdapter  extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
    private List<String> productSizes;
    private int selectedPosition = -1;
    private String size = null;

    public SizeAdapter(List<String> productSizes) {
        this.productSizes = productSizes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductSizeBinding binding = inflate(from(parent.getContext()), parent, false);
        return new ViewHolder(this::onSizeClick, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productSizes.get(position),
                selectedPosition == position,
                position == (productSizes.size() - 1));
    }

    @Override
    public int getItemCount() {
        return productSizes.size();
    }

    private void onSizeClick(String size) {
        selectedPosition = productSizes.indexOf(size);
        this.size = size;
        notifyDataSetChanged();
    }

    public String getSize() {
        return size;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterItemClickListener listener;
        private ProductSizeBinding binding;
        private MaterialButton btnSize;

        ViewHolder(AdapterItemClickListener listener, ProductSizeBinding binding) {
            super(binding.getRoot());
            this.listener = listener;
            this.binding = binding;
            this.btnSize = this.binding.productSize;
        }

        void bind(String size, Boolean checked, Boolean isLastIndex) {
            binding.setSize(size);
            binding.setLastIndex(isLastIndex);
            int btnColor = checked ? R.color.colorRed : R.color.colorLightGray;
            btnSize.setStrokeColorResource(btnColor);
            btnSize.setOnClickListener(view -> listener.onItemClick(size));
        }
    }
}
