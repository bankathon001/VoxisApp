package com.bankathon.voxisapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.bankathon.voxisapp.apis.response.Response;
import com.bankathon.voxisapp.apis.response.ValidatePinStatus;
import com.bankathon.voxisapp.util.AudioUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;

public class BankActivity extends Activity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        int count = 0;

        while (true) {

            AudioUtils.textToSpeech("Hello Neeraj, What you want to do today");
            String input = AudioUtils.speechToText();
            if (input == null) {
                AudioUtils.textToSpeech("Please say something");
                count++;
            } else if (input.toLowerCase().contains("balance")) {
                GetBalanceResponse response = fetchBalance();
                AudioUtils.textToSpeech("Your account balance is " + response.getBalance());
            } else if (input.toLowerCase().contains("last") || input.toLowerCase().contains("transactions")) {
                List<String> list = fetchLast5Transactions();
                list.stream().forEach(item -> {
                    AudioUtils.textToSpeech(item);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e(e.toString(), null);
                    }
                });
            } else if (input.toLowerCase().contains("credit")) {
                AudioUtils.textToSpeech("Your current due is " + "Rs " + String.format("%04d", new Random().nextInt(10000)));
            } else if (input.toLowerCase().contains("exit")) {
                AudioUtils.textToSpeech("Thank you for banking with voxis");
                break;
            } else if (input.toLowerCase().contains("customer") && input.toLowerCase().contains("call")) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9971971868"));
                startActivity(callIntent);
            } else {
                count++;
                AudioUtils.textToSpeech("Unable to understand the output");
            }
            if (count >= 3) {
                AudioUtils.textToSpeech("Maximum number of attempt finished, Thank you for banking with voxis");
                break;
            }
        }
        finishAndRemoveTask();
    }

    private List<String> fetchLast5Transactions() {
        AtomicReference<List<String>> response = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            Call<Response> last5txn =
                    AwsApiClient.getInstance().getMyApi().last5txn(AudioUtils.getMobileNumber());
            try {

                response.set((List<String>) last5txn.execute().body());
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

    private GetBalanceResponse fetchBalance() {
        AtomicReference<GetBalanceResponse> response = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            Call<GetBalanceResponse> generateCaptcha =
                    AwsApiClient.getInstance().getMyApi().getUserBalanceByMobileNumber(AudioUtils.getMobileNumber());
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
