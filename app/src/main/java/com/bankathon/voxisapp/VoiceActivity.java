package com.bankathon.voxisapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.request.RegisteredVoiceRequest;
import com.bankathon.voxisapp.apis.response.RegisteredVoiceStatus;
import com.bankathon.voxisapp.apis.response.Response;
import com.bankathon.voxisapp.util.AudioUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;

public class VoiceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);

        AudioUtils.textToSpeech("App is not register with any voice sample, Initiating the registration process");

        AudioUtils.textToSpeech("Please Input your voice sample");

        String inputFromUser = AudioUtils.speechToText();


        RegisteredVoiceStatus status = registerVoice(inputFromUser);
        if (status.equals(RegisteredVoiceStatus.REGISTERED)) {
            Intent i = new Intent(this.getApplicationContext(), RegistrationActivity.class);
            startActivity(i);
            finish();
        } else {
            AudioUtils.textToSpeech("Registration is unsuccessful, Try Again");
            Intent i = new Intent(this.getApplicationContext(), VoiceActivity.class);
            startActivity(i);
            finish();
        }
    }

    private RegisteredVoiceStatus registerVoice(String inputFromUser) {
        AtomicReference<RegisteredVoiceStatus> response = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            RegisteredVoiceRequest request = new RegisteredVoiceRequest();
            request.setMobileNumber("9582340663");
            request.setBase64EncodeSpeech(inputFromUser);

            Call<Response> generateCaptcha =
                    AwsApiClient.getInstance().getMyApi().registerVoice(request);
            try {

                response.set((RegisteredVoiceStatus) generateCaptcha.execute().body().getBody());
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