package com.bankathon.voxisapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.response.GetBalanceResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        textView = findViewById(R.id.tv_center);
        Button button = findViewById(R.id.button_act);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBalance();
            }
        });
    }

    private void fetchBalance() {
        Call<GetBalanceResponse> getBalanceResponseCall = AwsApiClient.getInstance().getMyApi().getUserBalanceByMobileNumber("9971971868");

        getBalanceResponseCall.enqueue(new Callback<GetBalanceResponse>() {
            @Override
            public void onResponse(Call<GetBalanceResponse> call, Response<GetBalanceResponse> response) {
                int bal = response.body().getBalance();
                textView.setText("Balance is " + bal);
            }

            @Override
            public void onFailure(Call<GetBalanceResponse> call, Throwable t) {
                textView.setText("failed to fetch");

            }
        });

    }
}
