package com.bankathon.voxisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bankathon.voxisapp.apis.AwsApiClient;
import com.bankathon.voxisapp.apis.response.GetBalanceResponse;
import com.bankathon.voxisapp.apis.response.RegisterCheck;
import com.bankathon.voxisapp.ui.login.LoginActivity;

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bankathon.voxisapp.util.BraodCaster.PLUGGEG_FLAG;

public class MainActivity extends AppCompatActivity {

    private final Logger logger = Logger.getLogger(MainActivity.class.getName());

    // TTS object implements TextToSpeech.OnInitListener
    //private TextToSpeech myTTS;

    private boolean isRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showImage();
        //This method is used so that your splash activity
        //can cover the entire screen.
        //myTTS = new TextToSpeech(this,  this::onInit);
        boolean jackIn = getAudioDevicesStatus();
        if(jackIn) {
            logger.info( "redirecting to Login Just After Opening");
            redirectIfJackConnected(jackIn);
        }
        //sayText();
        while(!jackIn) {
            logger.info( "Checked if Jack Plugged in or Not");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jackIn = getAudioDevicesStatus();
        }
        logger.info( "redirecting to Login");
        redirectIfJackConnected(jackIn);
    }

    private boolean getAudioDevicesStatus() {
        this.recreate();
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

/*    private void sayText() {
        logger.info( "text to speech init");
        myTTS.speak("Please Connect HeadPhoneJack", TextToSpeech.QUEUE_FLUSH, null, null);
    }*/

    private void redirectIfJackConnected(boolean val) {
        if (val) {
            TelephonyManager tMgr = (TelephonyManager)getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            check(mPhoneNumber);
            if(isRegistered) {
                Intent i = new Intent(MainActivity.this,
                        LoginActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
            } else {
                Intent i = new Intent(MainActivity.this,
                        LoginActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
            }
        }
    }

    private void check(String number) {
        Call<RegisterCheck> registerCheckCall =
                AwsApiClient.getInstance().getMyApi().checkIfRegistered("9971971868");

        registerCheckCall.enqueue(new Callback<RegisterCheck>() {
            @Override
            public void onResponse(Call<RegisterCheck> call, Response<RegisterCheck> response) {
                isRegistered = response.body().getFlag();
            }

            @Override
            public void onFailure(Call<RegisterCheck> call, Throwable t) {
                Log.e("error", "error is : " + t.getMessage());
            }
        });

    }

/*    private void showImage() {
        //this will bind your MainActivity.class file with activity_main.
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(4000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        image.startAnimation(animation);
    }*/
/*
    @Override
    public void onInit(int initStatus) {
        // check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            logger.info("TTS Success");
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                myTTS.setLanguage(Locale.US);
            }
        } else if (initStatus == TextToSpeech.ERROR) {
            logger.info("TTS Failed");
            Toast.makeText(this, "Sorry! Text To Speech failed...",
                    Toast.LENGTH_LONG).show();
        }
    }*/
}