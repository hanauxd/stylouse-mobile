package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.adapters.InquiriesPagerAdapter;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentInquiriesBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.services.InquiryService;

public class InquiriesFragment extends RootBaseFragment {
    private FragmentInquiriesBinding binding;
    private List<String> tabs = Arrays.asList("Inbox", "Unread");

    @Inject
    InquiryService inquiryService;
    @Inject
    AuthSession session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = FragmentInquiriesBinding.inflate(inflater, container, false);
      return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Inquiries");
        if (session.getAuthState() != null) {
            ViewPager2 viewPager = binding.viewPager;
            viewPager.setAdapter(new InquiriesPagerAdapter(this));
            new TabLayoutMediator(binding.tabLayout, viewPager, ((tab, position) -> {tab.setText(tabs.get(position));})).attach();
        }
    }
}