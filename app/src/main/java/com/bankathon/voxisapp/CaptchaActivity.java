package com.bankathon.voxisapp;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.response.Response;
import com.bankathon.voxisapp.util.AudioUtils;
import com.bankathon.voxisapp.util.TTSUtil;

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
        //TTSUtil ttsUtil = new TTSUtil();
        AudioUtils.textToSpeech("Please repeat to login " + captchaString);

        String inputFromUser = AudioUtils.speechToText();

        if (inputFromUser != null) {
            AudioUtils.textToSpeech(inputFromUser);
        } else {
            AudioUtils.textToSpeech("Not input found");
        }
    }

    private String getCaptcha() {
        AtomicReference<String> response = new AtomicReference<>("");
        Thread thread = new Thread(() -> {
            Call<Response> generateCaptcha =
                    AwsApiClient.getInstance().getMyApi().generateCaptcha("2345343234");
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

}