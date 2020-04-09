package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.apiit.eea.stylouse.databinding.OrderListItemBinding;
import lk.apiit.eea.stylouse.models.responses.OrderItemResponse;
import lk.apiit.eea.stylouse.models.responses.OrdersResponse;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrdersResponse> orders;

    public OrderAdapter(List<OrdersResponse> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListItemBinding binding = OrderListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
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

        ViewHolder(@NonNull OrderListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrdersResponse order) {
            binding.setDate(order.getDate());
            binding.setTotal(String.valueOf(total(order)));
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
