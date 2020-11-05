package com.bankathon.voxisapp.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioUtils {

        public static void convertBytesToFile(byte[] bytearray, Context context) {
            listPath();
            bytearray = getByteArrayFromAudio(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/sampleKB.mp3");
            try {
                File outputFile = File.createTempFile("", "mp3", getCacheDir(context));
                outputFile.deleteOnExit();
                FileOutputStream fileoutputstream = new FileOutputStream("tempMp3");
                fileoutputstream.write(bytearray);
                fileoutputstream.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private static void listPath() {
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
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filePath);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                for (int readNum; (readNum = fis.read(b)) != -1; ) {
                    bos.write(b, 0, readNum);
                }
                return bos.toByteArray();
            } catch (Exception e) {
                Log.d("mylog", e.toString());
            }
            return null;
        }


        private static File getCacheDir(Context context) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voxisDir";
            File storageDir = new File(path);
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                // This should never happen - log handled exception!
            }
            return storageDir;
        }


    }