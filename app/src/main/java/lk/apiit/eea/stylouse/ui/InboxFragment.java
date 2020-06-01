package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.adapters.InboxAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentInboxBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.models.responses.InquiryResponse;
import lk.apiit.eea.stylouse.services.InquiryService;
import retrofit2.Response;

import static lk.apiit.eea.stylouse.utils.Constants.TYPE_UNREAD;
import static lk.apiit.eea.stylouse.utils.Navigator.navigate;

public class InboxFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private FragmentInboxBinding binding;
    private List<Inquiry> inquiries;
    private String type;

    @Inject
    AuthSession session;
    @Inject
    InquiryService inquiryService;

    public InboxFragment(String type) {
        this.type = type;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.getAuthState() != null) {
            fetchInquiries();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        loading.setValue(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onInboxItemClick(String inquiryJSON) {
        Bundle bundle = new Bundle();
        bundle.putString("inquiry", inquiryJSON);
        navigate(parentNavController, R.id.action_adminFragment_to_replyFragment, bundle);
    }

    private void fetchInquiries() {
        error.setValue(null);
        loading.setValue(true);
        inquiryService.getAllInquiries(inquiriesCallback, session.getAuthState().getJwt());
    }

    private ApiResponseCallback inquiriesCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            InquiryResponse inquiryResponse = (InquiryResponse) response.body();
            if (inquiryResponse != null) {
                inquiries = inquiryResponse.getInquiries();
                setAdapter();
            }
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            error.setValue(message);
            DynamicToast.makeError(activity, message).show();
            loading.setValue(false);
        }
    };

    private void setAdapter() {
        InboxAdapter adapter = new InboxAdapter(getInquiriesList(), this::onInboxItemClick);
        binding.inboxList.setAdapter(adapter);
    }

    private List<Inquiry> getInquiriesList() {
        if (TYPE_UNREAD.equals(this.type)) {
            return getUnreadInquiries();
        }
        return this.inquiries;
    }

    private List<Inquiry> getUnreadInquiries() {
        List<Inquiry> unreadInquiries = new ArrayList<>();

        for (Inquiry inquiry : inquiries) {
            List<Reply> replies = inquiry.getReplies();

            int unreadReplyCount = 0;

            for (Reply reply : replies) {
                if (!reply.isRead() && !reply.getUser().getEmail().equals(session.getAuthState().getUserId())) {
                    unreadReplyCount++;
                }
            }

            if (unreadReplyCount > 0) {
                unreadInquiries.add(inquiry);
            }
        }

        return unreadInquiries;
    }
}