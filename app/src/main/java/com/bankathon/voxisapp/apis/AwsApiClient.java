package com.bankathon.voxisapp.apis;

import com.google.android.gms.common.api.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AwsApiClient {
    private static AwsApiClient instance = null;
    private AwsApi myApi;

    private AwsApiClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AwsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(AwsApi.class);
    }

    public static synchronized AwsApiClient getInstance() {
        if (instance == null) {
            instance = new AwsApiClient();
        }
        return instance;
    }

    public AwsApi getMyApi() {
        return myApi;
    }
}
