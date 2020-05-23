package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Map;

import lk.apiit.eea.stylouse.databinding.FragmentRateAverageBinding;
import lk.apiit.eea.stylouse.models.responses.ReviewResponse;

public class RateAverageFragment extends RootBaseFragment {
    private FragmentRateAverageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRateAverageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindAverageToView();
    }

    private ReviewResponse reviews() {
        String reviewJSON = getArguments() != null ? getArguments().getString("review") : null;
        return new Gson().fromJson(reviewJSON, ReviewResponse.class);
    }

    private void bindAverageToView() {
        ReviewResponse reviews = reviews();

        int size = reviews.getReviews().size();
        binding.setCount(String.valueOf(size));

        Map<String, Integer> count = reviews.getCount();
        if (size > 0) {
            binding.setOne((count.get("one") * 100) / size);
            binding.setTwo((count.get("two") * 100) / size);
            binding.setThree((count.get("three") * 100) / size);
            binding.setFour((count.get("four") * 100) / size);
            binding.setFive((count.get("five") * 100) / size);

            DecimalFormat decimalFormat = new DecimalFormat("##.0");
            String average = decimalFormat.format(reviews.getAverage());
            binding.setAverage(average);
        } else {
            binding.setAverage("0");
        }
    }
}
