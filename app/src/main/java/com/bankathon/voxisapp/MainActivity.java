package com.bankathon.voxisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankathon.voxisapp.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_main);
        //this will bind your MainActivity.class file with activity_main.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean jackIn = false;
                AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
                for(AudioDeviceInfo deviceInfo : audioDevices){
                    if(deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                            || deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADSET){
                        jackIn = true;
                    }
                }
                if(jackIn) {
                    Intent i = new Intent(MainActivity.this,
                            LoginActivity.class);
                    //Intent is used to switch from one activity to another.

                    startActivity(i);
                    //invoke the SecondActivity.

                    finish();
                    //the current activity will get finished.
                } else {
                     ImageView image = (ImageView) findViewById(R.id.imageView);
                    Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
                    animation.setDuration(4000); //1 second duration for each animation cycle
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
                    animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
                    image.startAnimation(animation); //to start animation

                   /* Intent i = new Intent(MainActivity.this,
                            LoginActivity.class);
                    //Intent is used to switch from one activity to another.

                    startActivity(i);
                    //invoke the SecondActivity.

                    finish();
                    //the current activity will get finished.*/
                }
            }
        }, 2000);
    }
}