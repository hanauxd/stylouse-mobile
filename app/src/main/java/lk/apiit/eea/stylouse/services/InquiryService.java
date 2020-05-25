package lk.apiit.eea.stylouse.services;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.InquiryAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.Reply;
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

    public void getInquiriesByUser(ApiResponseCallback callback, String jwt) {
        Call<InquiryResponse> inquiriesCall = inquiryAPI.getInquiriesByUser(StringFormatter.formatToken(jwt));
        inquiriesCall.enqueue(new RetroFitCallback<>(callback));
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
        Call<Reply> replyCall = inquiryAPI.createReply(StringFormatter.formatToken(jwt), request);
        replyCall.enqueue(new RetroFitCallback<>(callback));
    }
}
