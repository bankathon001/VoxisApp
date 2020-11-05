package com.bankathon.voxisapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.request.ValidatePinRequest;
import com.bankathon.voxisapp.apis.response.GetBalanceResponse;
import com.bankathon.voxisapp.apis.response.ValidatePinStatus;
import com.bankathon.voxisapp.util.AudioUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankActivity extends AppCompatActivity {
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

        AudioUtils.textToSpeech("What you want to do today");
        String input = AudioUtils.speechToText();
        if (input == null) {

        } else if (input.toLowerCase().contains("balance")) {
            GetBalanceResponse response = fetchBalance();
            AudioUtils.textToSpeech("Your account balance is " + response.getBalance());
        }
    }

    private GetBalanceResponse fetchBalance() {
        AtomicReference<GetBalanceResponse> response = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            Call<GetBalanceResponse> generateCaptcha =
                    AwsApiClient.getInstance().getMyApi().getUserBalanceByMobileNumber("9582340663");
            try {

                response.set(generateCaptcha.execute().body());
            } catch (IOException e) {
                Log.i(e.toString(), "");
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.i(e.toString(), "");
        }
        return response.get();

    }
}
