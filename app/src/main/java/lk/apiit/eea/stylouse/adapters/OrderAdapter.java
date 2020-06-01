package lk.apiit.eea.stylouse.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import lk.apiit.eea.stylouse.databinding.OrderListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.responses.OrderItemResponse;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;

import static android.view.LayoutInflater.from;
import static lk.apiit.eea.stylouse.databinding.OrderListItemBinding.inflate;
import static lk.apiit.eea.stylouse.utils.StringFormatter.formatCurrency;
import static lk.apiit.eea.stylouse.utils.StringFormatter.formatDate;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrdersResponse> orders;
    private AdapterItemClickListener clickListener;

    public OrderAdapter(List<OrdersResponse> orders, AdapterItemClickListener clickListener) {
        this.orders = orders;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListItemBinding binding = inflate(from(parent.getContext()), parent, false);
        return new ViewHolder(binding, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private OrderListItemBinding binding;
        private AdapterItemClickListener clickListener;

        ViewHolder(@NonNull OrderListItemBinding binding, AdapterItemClickListener clickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.clickListener = clickListener;
        }

        void bind(OrdersResponse order) {
            binding.setDate(formatDate(order.getDate()));
            binding.setTotal(formatCurrency(total(order)));
            binding.orderItem.setOnClickListener(v -> clickListener.onItemClick(new Gson().toJson(order)));
        }

        private double total(OrdersResponse order) {
            List<OrderItemResponse> orderItems = order.getOrderItems();
            double total = 0;
            for (OrderItemResponse orderItem : orderItems) {
                total += orderItem.getQuantity() * orderItem.getProduct().getPrice();
            }
            return total;
        }
    }
}
