package com.bankathon.voxisapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.request.AuthenticateVoiceRequest;
import com.bankathon.voxisapp.apis.response.Response;
import com.bankathon.voxisapp.apis.response.VoiceAuthenticateStatus;
import com.bankathon.voxisapp.util.AudioUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CaptchaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);

        final String captchaString = getCaptcha();

        AudioUtils.textToSpeech("Please repeat to login " + captchaString);

        int count = 0;
        String inputFromUser = "";
        while (true) {
            inputFromUser = AudioUtils.speechToText();
            if (inputFromUser != null) {
                //AudioUtils.textToSpeech(inputFromUser);
                int len = inputFromUser.length() > 2 ? inputFromUser.length() - 2 : inputFromUser.length();
                if (!inputFromUser.substring(0, len).equalsIgnoreCase(captchaString.substring(0, len))) {
                    count++;
                    AudioUtils.textToSpeech("Wrong Input try again by saying " + captchaString);
                } else {
                    break;
                }
            } else {
                count++;
                AudioUtils.textToSpeech("No Input Try again");
            }
            if (count >= 3) {
                AudioUtils.textToSpeech("Maximum number of attempt finished, Thank you for banking with voxis");
                break;
            }
        }
        if (count >= 3) {
            finishAndRemoveTask();
        }
        VoiceAuthenticateStatus status = authenticateVoice(inputFromUser, captchaString);
        if (status.equals(VoiceAuthenticateStatus.ACCEPTED)) {
            Intent i = new Intent(this.getApplicationContext(), BankActivity.class);
            startActivity(i);
            finish();
        } else {
            AudioUtils.textToSpeech("Authentication is unsuccessful, Try Again");
            Intent i = new Intent(this.getApplicationContext(), CaptchaActivity.class);
            startActivity(i);
            finish();
        }
    }

    private String getCaptcha() {
        AtomicReference<String> response = new AtomicReference<>("");
        Thread thread = new Thread(() -> {
            Call<Response> generateCaptcha =
                    AwsApiClient.getInstance().getMyApi().generateCaptcha(AudioUtils.getMobileNumber());
            try {
                response.set((String) generateCaptcha.execute().body().getBody());
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

    private VoiceAuthenticateStatus authenticateVoice(String inputFromUser, String captchaString) {
        AtomicReference<VoiceAuthenticateStatus> response = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            AuthenticateVoiceRequest request = new AuthenticateVoiceRequest();
            request.setCaptcha(captchaString);
            request.setMobileNumber(AudioUtils.getMobileNumber());
            request.setBase64EncodeSpeech(inputFromUser);

            Call<Response> generateCaptcha =
                    AwsApiClient.getInstance().getMyApi().authenticateVoice(request);
            try {

                response.set(VoiceAuthenticateStatus.valueOf((String) generateCaptcha.execute().body().getBody()));
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