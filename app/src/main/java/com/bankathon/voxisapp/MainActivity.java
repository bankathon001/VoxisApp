package com.bankathon.voxisapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.response.RegisterCheck;
import com.bankathon.voxisapp.ui.login.LoginActivity;
import com.bankathon.voxisapp.util.AudioUtils;
import com.bankathon.voxisapp.util.TTSUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import retrofit2.Call;

import static android.Manifest.permission.INTERNET;

public class MainActivity extends Activity {

    private final Logger logger = Logger.getLogger(MainActivity.class.getName());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{INTERNET}, 10);

        AudioUtils.textToSpeech("Welcome to Voxis Voice Banking Application");
        boolean jackIn = getAudioDevicesStatus();
        if(jackIn) {
            logger.info( "redirecting to Login Just After Opening");
            try {
                redirectIfJackConnected(true);
            } catch (Exception e) {
                logger.info(e.toString());
            }
        } else {
            while (!jackIn) {
                AudioUtils.textToSpeech("Please Connect Headset to Continue");
                logger.info("Checked if Jack Plugged in or Not");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.info(e.toString());
                }
                jackIn = getAudioDevicesStatus();
            }
            logger.info("redirecting to Login");
            try {
                redirectIfJackConnected(jackIn);
            } catch (Exception e) {
                logger.info(e.toString());
            }
        }
    }



    private boolean getAudioDevicesStatus() {
        //this.recreate();
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for(AudioDeviceInfo deviceInfo : audioDevices){
            if(deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    || deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADSET){
               return true;
            }
        }
        return false;
    }


    private void redirectIfJackConnected(boolean val) throws Exception{
        if (true) {
            TelephonyManager tMgr = (TelephonyManager)getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
            AtomicBoolean flag = new AtomicBoolean(false);
            Thread thread = new Thread(() ->{
                try {
                   flag.set(check("2345343234"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            thread.join();

            /// Check if registered
            if(flag.get()) {
                Intent i = new Intent(this.getApplicationContext(),
                        CaptchaActivity.class);
                //Intent is used to switch from one activity to another.
                startActivity(i);
                //invoke the SecondActivity.

                finish();
            } else {
                Intent i = new Intent(this.getApplicationContext(),
                        VoiceActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
            }
        }
    }

    private boolean check(String number) throws IOException {
        Call<RegisterCheck> registerCheckCall =
                AwsApiClient.getInstance().getMyApi().checkIfRegistered(number);
        return registerCheckCall.execute().body().getFlag();
    }
}