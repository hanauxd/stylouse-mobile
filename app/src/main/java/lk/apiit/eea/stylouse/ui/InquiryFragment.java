package lk.apiit.eea.stylouse.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.adapters.InquiryAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentInquiryBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.models.requests.InquiryRequest;
import lk.apiit.eea.stylouse.models.responses.InquiryResponse;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.InquiryService;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeError;
import static java.util.stream.Collectors.toList;
import static lk.apiit.eea.stylouse.databinding.FragmentInquiryBinding.inflate;

public class InquiryFragment extends RootBaseFragment {
    private MutableLiveData<InquiryResponse> inquiryData = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private FragmentInquiryBinding binding;
    private InquiryResponse inquiryResponse;

    @Inject
    AuthSession session;
    @Inject
    InquiryService inquiryService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inquiryData.observe(getViewLifecycleOwner(), this::onInquiryChange);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        binding.btnSend.setOnClickListener(this::onSendClick);
        binding.btnSend.setColorFilter(Color.WHITE);
        fetchInquiryByProduct();
    }

    private void onInquiryChange(InquiryResponse response) {
        if (response != null) {
            List<Inquiry> inquiries = response.getInquiries();
            List<Reply> replies = new ArrayList<>();
            inquiries.forEach(inquiry -> replies.addAll(inquiry.getReplies()));
            InquiryAdapter adapter = new InquiryAdapter(replies);
            binding.inquiryList.setAdapter(adapter);
        }
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onSendClick(View view) {
        String inquiryText = binding.inquiry.getText().toString();
        if (isEmpty(inquiryText)) {
            makeText(activity, "Message is required", LENGTH_SHORT).show();
        } else {
            InquiryRequest inquiry = new InquiryRequest(product().getId(), inquiryText);
            inquiryService.createInquiry(createCallback, session.getAuthState().getJwt(), inquiry);
        }
    }

    private ProductResponse product() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        return new Gson().fromJson(productJSON, ProductResponse.class);
    }

    private void fetchInquiryByProduct() {
        loading.setValue(true);
        inquiryService.getInquiryByProduct(inquiryCallback, session.getAuthState().getJwt(), product().getId());
    }

    private void markAsRead() {
        List<Inquiry> userInquiries = inquiryResponse
                .getInquiries()
                .stream()
                .filter(inquiry -> inquiry.getUser().getEmail().equals(session.getAuthState().getUserId()))
                .collect(toList());

        if (userInquiries.size() > 0) {
            List<Reply> replies = userInquiries
                    .get(0)
                    .getReplies()
                    .stream()
                    .filter(reply -> !reply.isRead() && !reply.getUser().getId().equals(session.getAuthState().getUserId()))
                    .collect(toList());

            List<String> replyIds = new ArrayList<>();
            replies.forEach(reply -> {
                replyIds.add(reply.getId());
            });

            inquiryService.markAsRead(readCallback, session.getAuthState().getJwt(), replyIds);
        }
    }

    private ApiResponseCallback createCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            inquiryResponse = (InquiryResponse) response.body();
            inquiryData.setValue(inquiryResponse);
            binding.inquiry.setText("");
        }

        @Override
        public void onFailure(String message) {
            makeError(activity, message).show();
        }
    };

    private ApiResponseCallback readCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            //Do nothing
        }

        @Override
        public void onFailure(String message) {
            makeError(activity, message).show();
        }
    };

    private ApiResponseCallback inquiryCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            inquiryResponse = (InquiryResponse) response.body();
            inquiryData.setValue(inquiryResponse);
            loading.setValue(false);
            markAsRead();
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            makeError(activity, message).show();
        }
    };
}
