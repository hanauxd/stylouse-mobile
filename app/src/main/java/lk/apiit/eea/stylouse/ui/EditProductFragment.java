package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.apiit.eea.stylouse.databinding.FragmentEditProductBinding;

public class EditProductFragment extends RootBaseFragment {
    private FragmentEditProductBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


}
