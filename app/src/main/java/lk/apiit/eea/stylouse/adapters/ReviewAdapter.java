package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.ReviewListItemBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.interfaces.AdapterItemClickListener;
import lk.apiit.eea.stylouse.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    @Inject
    AuthSession session;
    private List<Review> reviews;
    private AdapterItemClickListener listener;

    public ReviewAdapter(List<Review> reviews, AdapterItemClickListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReviewListItemBinding binding = ReviewListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ((StylouseApp) binding.getRoot().getContext().getApplicationContext()).getAppComponent().inject(this);
        return new ViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(reviews.get(position), position == 0);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ReviewListItemBinding binding;
        private AdapterItemClickListener listener;
        private Review review;

        ViewHolder(ReviewListItemBinding binding, AdapterItemClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        void bind(Review review, Boolean isLastItem) {
            this.review = review;
            binding.setReview(review);
            binding.setIsLastItem(isLastItem);
            if (listener != null) {
                binding.setRemovable(true);
                binding.btnRemove.setOnClickListener(this::onRemoveClick);
            }
        }

        private void onRemoveClick(View view) {
            String reviewJSON = new Gson().toJson(review);
            listener.onItemClick(reviewJSON);
        }
    }
}
