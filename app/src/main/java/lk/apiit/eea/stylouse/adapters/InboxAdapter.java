package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.InboxListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.utils.UrlBuilder;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {
    private List<Inquiry> inquiries;
    private AdapterItemClickListener listener;

    @Inject
    UrlBuilder urlBuilder;

    public InboxAdapter(List<Inquiry> inquiries, AdapterItemClickListener listener) {
        this.inquiries = inquiries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InboxListItemBinding binding = InboxListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ((StylouseApp)binding.getRoot().getContext().getApplicationContext()).getAppComponent().inject(this);
        return new ViewHolder(binding, listener, urlBuilder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(inquiries.get(position));
    }

    @Override
    public int getItemCount() {
        return inquiries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private InboxListItemBinding binding;
        private AdapterItemClickListener itemClickListener;
        private UrlBuilder urlBuilder;

        public ViewHolder(@NonNull InboxListItemBinding binding, AdapterItemClickListener itemClickListener, UrlBuilder urlBuilder) {
            super(binding.getRoot());
            this.binding = binding;
            this.itemClickListener = itemClickListener;
            this.urlBuilder = urlBuilder;
        }

        void bind(Inquiry inquiry) {
            Glide.with(binding.getRoot())
                    .load(urlBuilder.fileUrl(inquiry.getProduct().getProductImages().get(0).getFilename()))
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.stylouse_placeholder)
                    .into(binding.image);

            binding.setTitle(inquiry.getProduct().getName());
            binding.setUser(inquiry.getUser().getLastName());
            binding.getRoot().setOnClickListener(view -> itemClickListener.onItemClick(new Gson().toJson(inquiry)));
        }
    }
}
