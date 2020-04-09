package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import lk.apiit.eea.stylouse.databinding.FragmentShippingBinding;

public class ShippingFragment extends Fragment {
    private FragmentShippingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShippingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
