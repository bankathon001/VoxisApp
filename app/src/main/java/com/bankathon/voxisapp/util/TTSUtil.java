package com.bankathon.voxisapp.util;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TTSUtil {

    TextToSpeech tts;

    public void textToSpeech(String text, Activity appCompatActivity) {
        tts = new TextToSpeech(appCompatActivity.getApplicationContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.UK);
            }
        });
        ConvertTextToSpeech(text);
    }

    private void ConvertTextToSpeech(String text) {
        if ("".equals(text)) {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak(text + " is saved", TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
