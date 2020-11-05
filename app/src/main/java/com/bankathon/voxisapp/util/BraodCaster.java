package com.bankathon.voxisapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bankathon.voxisapp.MainActivity;

import java.util.logging.Logger;

public class BraodCaster extends BroadcastReceiver {

    private final Logger logger = Logger.getLogger(BraodCaster.class.getName());

    private String TAG = "HeadSet";

    public BraodCaster() {
        logger.info(TAG + "Created");
    }

    public static Boolean PLUGGEG_FLAG = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch(state) {
                case(0):
                    logger.info(TAG + "Headset unplugged");
                    PLUGGEG_FLAG = false;
                    break;
                case(1):
                    logger.info(TAG + "Headset plugged");
                    PLUGGEG_FLAG = true;
                    break;
                default:
                    logger.info(TAG + "Error");
            }
        }
    }

}