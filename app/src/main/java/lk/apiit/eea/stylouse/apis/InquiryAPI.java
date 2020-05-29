package lk.apiit.eea.stylouse.apis;

import java.util.List;

import lk.apiit.eea.stylouse.models.Inquiry;
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
    Call<InquiryResponse> getInquiryByProduct(@Header("Authorization") String token, @Path("id") String id);

    @POST("inquiries")
    Call<InquiryResponse> createInquiry(@Header("Authorization") String token, @Body InquiryRequest request);

    @POST("replies")
    Call<Inquiry> createReply(@Header("Authorization") String token, @Body InquiryRequest request);

    @GET("inquiries/all")
    Call<InquiryResponse> getAllInquiries(@Header("Authorization") String token);

    @POST("replies/read")
    Call<InquiryResponse> markAsRead(@Header("Authorization") String token, @Body List<String> replyIds);
}
