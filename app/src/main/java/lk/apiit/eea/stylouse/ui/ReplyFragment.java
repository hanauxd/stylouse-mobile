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
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.adapters.InquiryAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentInquiryBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.models.requests.InquiryRequest;
import lk.apiit.eea.stylouse.services.InquiryService;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static lk.apiit.eea.stylouse.databinding.FragmentInquiryBinding.inflate;

public class ReplyFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private MutableLiveData<List<Reply>> replyData = new MutableLiveData<>(new ArrayList<>());
    private FragmentInquiryBinding binding;

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
        binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        binding.btnSend.setColorFilter(Color.WHITE);
        markAsRead();
        replyData.observe(getViewLifecycleOwner(), this::onInquiryDateChange);
        replyData.setValue(inquiry().getReplies());
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onInquiryDateChange(List<Reply> replies) {
        binding.inquiryList.setAdapter(new InquiryAdapter(replies));
        binding.btnSend.setOnClickListener(this::onSendClick);
    }

    private void markAsRead() {
        List<Reply> collect = inquiry().getReplies().stream().filter(
                reply -> !reply.isRead() && !reply.getUser().getId().equals(session.getAuthState().getUserId())
        ).collect(
                Collectors.toList()
        );
        List<String> replyIds = new ArrayList<>();
        collect.forEach(reply -> {
            replyIds.add(reply.getId());
        });
        inquiryService.markAsRead(readCallback, session.getAuthState().getJwt(), replyIds);
    }

    private void onSendClick(View view) {
        String inquiry = binding.inquiry.getText().toString();
        if (isEmpty(inquiry)) {
            makeText(activity, "Reply is required", LENGTH_SHORT).show();
        } else {
            loading.setValue(true);
            InquiryRequest replyRequest = new InquiryRequest(inquiry().getId(), inquiry);
            inquiryService.createReply(replyCallback, session.getAuthState().getJwt(), replyRequest);
        }
    }

    private Inquiry inquiry() {
        String inquiryJSON = getArguments() != null ? getArguments().getString("inquiry") : null;
        return new Gson().fromJson(inquiryJSON, Inquiry.class);
    }

    private ApiResponseCallback replyCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            loading.setValue(false);
            binding.inquiry.setText("");
            Inquiry inquiry = (Inquiry) response.body();
            if (inquiry != null) {
                replyData.setValue(inquiry.getReplies());
            }
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            DynamicToast.makeError(activity, message).show();
        }
    };
    private ApiResponseCallback readCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            //Do nothing
        }

        @Override
        public void onFailure(String message) {
            DynamicToast.makeError(activity, message).show();
        }
    };
}