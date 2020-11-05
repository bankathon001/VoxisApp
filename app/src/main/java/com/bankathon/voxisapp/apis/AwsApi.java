package com.bankathon.voxisapp.apis;

import com.bankathon.voxisapp.apis.response.GetBalanceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AwsApi {
    String BASE_URL = "http://3.6.39.249:8080/api/voxis/v1/";


    @GET("get-balance")
    Call<GetBalanceResponse> getUserBalanceByMobileNumber(@Query("mobileNumber") String mobileNumber);
}
