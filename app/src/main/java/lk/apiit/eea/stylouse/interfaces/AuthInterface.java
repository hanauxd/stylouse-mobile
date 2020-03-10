package lk.apiit.eea.stylouse.interfaces;

import lk.apiit.eea.stylouse.dto.requests.AuthRequest;
import lk.apiit.eea.stylouse.dto.responses.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthInterface {
    @POST("/login")
    Call<AuthResponse> login(@Body AuthRequest request);
}
