package com.bankathon.voxisapp.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AudioUtils {
    private MediaPlayer mediaPlayer = new MediaPlayer();

    public static void convertBytesToFile(byte[] bytearray, Context context) {

        //listPath();
        bytearray = getByteArrayFromAudio(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/st.wav");
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
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: " + file.length);
        for (int i = 0; i < file.length; i++) {
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
        return new File(path + "/aud-" + System.currentTimeMillis() + ".wav");
    }

    static String speechSubscriptionKey = "e204c71506fd4bfd8a1a1f861088a35c";
    static String serviceRegion = "eastus";
    static SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);

    public static void textToSpeech(String text) {
        //TextView outputMessage = this.findViewById(R.id.outputMessage);
        //EditText speakText = this.findViewById(R.id.speakText);

        try {
            assert (speechConfig != null);

            SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig);
            // Note: this will block the UI thread, so eventually, you want to register for the event
            SpeechSynthesisResult result = synthesizer.SpeakText(text);
            assert (result != null);


            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                //outputMessage.setText("Speech synthesis succeeded.");
            } else if (result.getReason() == ResultReason.Canceled) {
                String cancellationDetails =
                        SpeechSynthesisCancellationDetails.fromResult(result).toString();
                /*outputMessage.setText("Error synthesizing. Error detail: " +
                        System.lineSeparator() + cancellationDetails +
                        System.lineSeparator() + "Did you update the subscription info?");*/
            }

            result.close();
        } catch (Exception ex) {
            Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
            //assert(false);
        }
    }

    public static String speechToText() {
        SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig);
        // Starts recognizing.
        System.out.println("Say something...");

        // Starts recognition. It returns when the first utterance has been recognized.
        SpeechRecognitionResult result = null;
        try {
            result = recognizer.recognizeOnceAsync().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        // Checks result.
        if (result.getReason() == ResultReason.RecognizedSpeech) {

            System.out.println("RECOGNIZED: Text=" + result.getText());
            return result.getText();
        } else if (result.getReason() == ResultReason.NoMatch) {
            System.out.println("NOMATCH: Speech could not be recognized.");
        } else if (result.getReason() == ResultReason.Canceled) {
            CancellationDetails cancellation = CancellationDetails.fromResult(result);
            System.out.println("CANCELED: Reason=" + cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                System.out.println("CANCELED: Did you update the subscription info?");
            }
        }

        result.close();
        return null;
    }

}