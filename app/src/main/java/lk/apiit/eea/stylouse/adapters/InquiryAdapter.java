package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.apiit.eea.stylouse.databinding.InquiryListItemBinding;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.utils.StringFormatter;

public class InquiryAdapter extends RecyclerView.Adapter<InquiryAdapter.ViewHolder> {
    private List<Reply> replies;

    public InquiryAdapter(List<Reply> replies) {
        this.replies = replies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InquiryListItemBinding binding = InquiryListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(replies.get(position));
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private InquiryListItemBinding binding;

        public ViewHolder(@NonNull InquiryListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reply reply) {
            binding.setRole(reply.getUser().getRole());
            binding.setMessage(reply.getMessage());
            binding.setDate(StringFormatter.formatDate(reply.getDate()));
            binding.setName(reply.getUser().getLastName());
        }
    }
}
