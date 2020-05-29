package lk.apiit.eea.stylouse.services;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.InquiryAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.requests.InquiryRequest;
import lk.apiit.eea.stylouse.models.responses.InquiryResponse;
import lk.apiit.eea.stylouse.utils.StringFormatter;
import retrofit2.Call;
import retrofit2.Retrofit;

public class InquiryService {
    private InquiryAPI inquiryAPI;

    @Inject
    public InquiryService(Retrofit retrofit) {
        this.inquiryAPI = retrofit.create(InquiryAPI.class);
    }

    public void getInquiryByProduct(ApiResponseCallback callback, String jwt, String productId) {
        Call<InquiryResponse> inquiryCall = inquiryAPI.getInquiryByProduct(StringFormatter.formatToken(jwt), productId);
        inquiryCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void createInquiry(ApiResponseCallback callback, String jwt, InquiryRequest request) {
        Call<InquiryResponse> inquiryCall = inquiryAPI.createInquiry(StringFormatter.formatToken(jwt), request);
        inquiryCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void createReply(ApiResponseCallback callback, String jwt, InquiryRequest request) {
        Call<Inquiry> replyCall = inquiryAPI.createReply(StringFormatter.formatToken(jwt), request);
        replyCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void getAllInquiries(ApiResponseCallback callback, String jwt) {
        Call<InquiryResponse> inquiriesCall = inquiryAPI.getAllInquiries(StringFormatter.formatToken(jwt));
        inquiriesCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void markAsRead(ApiResponseCallback callback, String jwt, List<String> replyIds) {
        Call<InquiryResponse> readCall = inquiryAPI.markAsRead(StringFormatter.formatToken(jwt), replyIds);
        readCall.enqueue(new RetroFitCallback<>(callback));
    }
}
