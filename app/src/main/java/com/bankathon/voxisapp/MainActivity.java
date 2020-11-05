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
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bankathon.voxisapp.ui.login.LoginActivity;

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.logging.Logger;

import static com.bankathon.voxisapp.util.BraodCaster.PLUGGEG_FLAG;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final Logger logger = Logger.getLogger(MainActivity.class.getName());

    // TTS object
    private TextToSpeech myTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        showImage();
        //This method is used so that your splash activity
        //can cover the entire screen.
        //myTTS = new TextToSpeech(this,  this::onInit);
        boolean jackIn = getAudioDevicesStatus();
        if(jackIn) {
            logger.info( "redirecting to Login Just After Opening");
            redirectIfJackConnected(jackIn);
        }
        sayText();
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

    private void sayText() {
        logger.info( "text to speech init");
        myTTS.speak("Please Connect HeadPhoneJack", TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void redirectIfJackConnected(boolean val) {
        if (val) {
            Intent i = new Intent(MainActivity.this,
                    LoginActivity.class);
            //Intent is used to switch from one activity to another.

            startActivity(i);
            //invoke the SecondActivity.

            finish();
        }
    }

    private void showImage() {
        //this will bind your MainActivity.class file with activity_main.
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(4000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        image.startAnimation(animation);
    }

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
    }
}