package lk.apiit.eea.stylouse.apis;

import lk.apiit.eea.stylouse.models.Inquiry;
import lk.apiit.eea.stylouse.models.Reply;
import lk.apiit.eea.stylouse.models.requests.InquiryRequest;
import lk.apiit.eea.stylouse.models.responses.InquiryResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InquiryAPI {
    @GET("inquiries/user")
    Call<InquiryResponse> getInquiriesByUser(@Header("Authorization") String token);

    @GET("inquiries/product/{id}")
    Call<Inquiry> getInquiryByUserAndProduct(@Header("Authorization") String token, @Path("id") String id);

    @POST("inquiries")
    Call<Inquiry> createInquiry(@Header("Authorization") String token, @Body InquiryRequest request);

    @POST("inquiries")
    Call<Reply> createReply(@Header("Authorization") String token, @Body InquiryRequest request);
}
