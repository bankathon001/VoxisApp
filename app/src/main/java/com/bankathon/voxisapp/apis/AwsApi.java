package com.bankathon.voxisapp.apis;

import com.bankathon.voxisapp.apis.request.AuthenticateVoiceRequest;
import com.bankathon.voxisapp.apis.request.RegisteredVoiceRequest;
import com.bankathon.voxisapp.apis.response.GetBalanceResponse;
import com.bankathon.voxisapp.apis.response.RegisterCheck;
import com.bankathon.voxisapp.apis.response.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AwsApi {
    String BASE_URL = "http://3.6.39.249:8080/api/voxis/v1/";


    @GET("get-balance")
    Call<GetBalanceResponse> getUserBalanceByMobileNumber(@Query("mobileNumber") String mobileNumber);

    @GET("is-registered")
    Call<RegisterCheck> checkIfRegistered(@Query("mobileNumber") String mobileNumber);

    @POST("authenticate-voice")
    Call<Response> authenticateVoice(@Body AuthenticateVoiceRequest authenticateVoiceRequest);

    @POST("register-voice")
    Call<Response> registerVoice(@Body RegisteredVoiceRequest registeredVoiceRequest);

    @GET("generate-capta")
    Call<Response> generateCaptcha(@Query("mobileNumber") String mobileNumber);

    @GET("last-5-txn")
    Call<Response> last5txn(@Query("mobileNumber") String mobileNumber);

    @GET("speech-to-text")
    Call<Response> stt(@Query("base64EncodeSpeech") String base64EncodeSpeech);

    @GET("text-to-speech")
    Call<Response> tts(@Query("text") String text);


}
