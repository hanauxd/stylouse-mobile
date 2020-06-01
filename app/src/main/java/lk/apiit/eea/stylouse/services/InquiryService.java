package lk.apiit.eea.stylouse.services;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.InquiryAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.requests.InquiryRequest;
import lk.apiit.eea.stylouse.models.responses.InquiryResponse;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

public class InquiryService {
    private InquiryAPI inquiryAPI;

    @Inject
    public InquiryService(Retrofit retrofit) {
        this.inquiryAPI = retrofit.create(InquiryAPI.class);
    }

    public void getInquiryByProduct(ApiResponseCallback callback, String jwt, String productId) {
        Call<InquiryResponse> inquiryCall = inquiryAPI.getInquiryByProduct(formatToken(jwt), productId);
        inquiryCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void createInquiry(ApiResponseCallback callback, String jwt, InquiryRequest request) {
        Call<InquiryResponse> inquiryCall = inquiryAPI.createInquiry(formatToken(jwt), request);
        inquiryCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void createReply(ApiResponseCallback callback, String jwt, InquiryRequest request) {
        Call<Inquiry> replyCall = inquiryAPI.createReply(formatToken(jwt), request);
        replyCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void getAllInquiries(ApiResponseCallback callback, String jwt) {
        Call<InquiryResponse> inquiriesCall = inquiryAPI.getAllInquiries(formatToken(jwt));
        inquiriesCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void markAsRead(ApiResponseCallback callback, String jwt, List<String> replyIds) {
        Call<InquiryResponse> readCall = inquiryAPI.markAsRead(formatToken(jwt), replyIds);
        readCall.enqueue(new RetroFitCallback<>(callback));
    }
}
