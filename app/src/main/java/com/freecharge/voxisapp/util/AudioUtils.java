package com.freecharge.voxisapp.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Locale;

public class AudioUtils {
    private MediaPlayer mediaPlayer = new MediaPlayer();

    public static void convertBytesToFile(byte[] bytearray, Context context) {

        //listPath();
        bytearray = getByteArrayFromAudio(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/st.wav");
        try {
            //File outputFile = File.createTempFile("", "mp3", getCacheDir(context));
            FileOutputStream fos = new FileOutputStream(getCacheDir(context));
            fos.write(bytearray);
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = new File("");//File.createTempFile("kurchina", "mp3", "");
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    private static void listPath() {
        Log.i("", "getStorageDirectory : " + Environment.getStorageDirectory().getAbsolutePath());
        Log.i("", "getStorageDirectory : " + Environment.getDataDirectory().getAbsolutePath());
        Log.i("", "getStorageDirectory : " + Environment.getRootDirectory().getAbsolutePath());
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
        }
    }

    public static byte[] getByteArrayFromAudio(String filePath) {
        File file = new File(filePath);
        byte[] data = new byte[(int) (file).length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static File getCacheDir(Context context) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voxisDir";
        File storageDir = new File(path);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
            // This should never happen - log handled exception!
        }
        return new File(path + "aud-" + System.currentTimeMillis() + ".wav");
    }


}